package messenger.proj.services;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import messenger.proj.DTO.ChatRoomDTO;
import messenger.proj.models.ChatRoom;
import messenger.proj.models.User;
import messenger.proj.repositories.ChatRoomRepository;

@Service
@Transactional(readOnly = true)
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final UserService userService;
	private final ModelMapper modelMapper;

	
	@Autowired
	public ChatRoomService(UserService userService, 
						ModelMapper modelMapper,
			ChatRoomRepository chatRoomRepository) {
		this.chatRoomRepository = chatRoomRepository;
		this.modelMapper = modelMapper;
		this.userService = userService;
	}
	
	@Transactional
	public void edit(ChatRoom chatRoom) {
		chatRoomRepository.save(chatRoom);
	}

	public Optional<ChatRoom> findById(String chatId) {
		return chatRoomRepository.findById(chatId);
	}
	
	public ChatRoom convertToChatRoom(ChatRoomDTO chatRoomDTO) {
		return modelMapper.map(chatRoomDTO, ChatRoom.class);
	}
	
	public ChatRoomDTO convertToChatRoomDTO(ChatRoom chatRoom) {
		return modelMapper.map(chatRoom, ChatRoomDTO.class);
	}

	@Transactional
	public void save(ChatRoomDTO chatRoomDTO) {
		
		if (!findAll().contains(convertToChatRoom(chatRoomDTO))) {
			
			ChatRoom chatRoom = new ChatRoom();
			
			chatRoom.setId(UUID.randomUUID().toString());
			
			Optional<User> recipientUser = userService.findById(chatRoomDTO.getRecipientId());
			Optional<User> senderUser = userService.findById(chatRoomDTO.getSenderId());
			
			if (recipientUser.isPresent()) {
				chatRoom.setRecipientName(recipientUser.get().getUsername());
			}
			
			if (senderUser.isPresent()) {
				chatRoom.setSenderName(senderUser.get().getUsername());
			}
 			
			
			chatRoomRepository.save(chatRoom);
			
		}
	}

	public Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId) {
		Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId);
		return chatRoom;
	}

	public Map<String, ChatRoom> findAllHashMap(String userId) {

		List<ChatRoom> listOne = chatRoomRepository.findBySenderId(userId);
		List<ChatRoom> listTwo = chatRoomRepository.findByRecipientId(userId);

		List<ChatRoom> newList = new ArrayList<>();
		newList.addAll(listOne);
		newList.addAll(listTwo);

		Map<String, ChatRoom> chats = new HashMap<>();

		for (ChatRoom chatRoom : newList) {
			chats.put(chatRoom.getId(), chatRoom);
		}

		return chats;
	}

	public List<ChatRoomDTO> findUsersChats(String userId) {
		return Stream
				.concat(chatRoomRepository.findBySenderId(userId).stream().map(x -> convertToChatRoomDTO(x)), 
						chatRoomRepository.findByRecipientId(userId).stream().map(x -> convertToChatRoomDTO(x)))
				.collect(Collectors.toList());
	}

	public List<ChatRoom> findAll() {
		return chatRoomRepository.findAll();
	}

	@Transactional
	public void deleteById(String chatId) {
		chatRoomRepository.deleteById(chatId);
	}



}

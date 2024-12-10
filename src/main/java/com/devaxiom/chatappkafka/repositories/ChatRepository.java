//package com.devaxiom.chatappkafka.repositories;
//
//import com.devaxiom.whatsapp_clone.model.Chat;
//import com.devaxiom.whatsapp_clone.model.Users;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ChatRepository extends JpaRepository<Chat, Long> {
//    @Query("SELECT c FROM Chat c WHERE c.isGroupChat = false AND :user MEMBER OF c.users AND :reqUser MEMBER OF c.users")
//    public Chat findSingleChatByUsersId(@Param("user")Users user, @Param("reqUser") Users reqUser);
//
//    @Query("SELECT c FROM Chat c JOIN c.users u WHERE u.id = :userId")
//    public List<Chat> findAllChatByUsersId(@Param("userId") Long userId);
//
//}

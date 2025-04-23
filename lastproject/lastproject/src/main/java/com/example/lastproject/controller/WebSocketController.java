package com.example.lastproject.controller;

import com.example.lastproject.model.requests.ChatMessage;
import com.example.lastproject.model.requests.LockLineRequest;
import com.example.lastproject.model.requests.UpdateCharacter;
import com.example.lastproject.model.requests.UserEnterRequest;
import com.example.lastproject.service.StringRedisService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Map;

@Controller
public class WebSocketController {
    private final StringRedisService redisService;
    public WebSocketController(StringRedisService redisService){
        this.redisService = redisService;
    }


    @MessageMapping("/project/{projectId}")
    @SendTo("/topic/project/{projectId}")
    public UpdateCharacter sendMessage(UpdateCharacter message)  {
        return message;
    }
    @MessageMapping("/project/{projectId}/chat")
    @SendTo("/topic/project/{projectId}/chat")
    public ChatMessage sendMessageTochat(ChatMessage message, @Header("simpSessionAttributes") Map<String, Object> sessionAttr) throws Exception {
        message.setId(Integer.parseInt((String)sessionAttr.get("id")));
        message.setUsername(((String)sessionAttr.get("username")));
        return message;
    }
    @MessageMapping("/project/{projectId}/{userId}/cursor")
    @SendTo("/topic/project/{projectId}/{userId}/cursor")
    public boolean saveCursor(@DestinationVariable int projectId, LockLineRequest lockLineRequest) {
        int currentLine =lockLineRequest.getLineNumber();
       String line = projectId+"/line:"+currentLine;
       String id = projectId+"/id:"+lockLineRequest.getUserId();
       if(redisService.getValue(line)!=null)
       {
           if(redisService.getValue(line).equals(id)){
               return true;
           }
           else {
               return false;}
       }
       else{
           String previousLineKey = redisService.getValue(id);
           if (previousLineKey != null) {
               redisService.deleteValue(previousLineKey);
               redisService.deleteValue(id);
           }
           redisService.setValue(line,id);
           redisService.setValue(id,line);
           return true;
       }
    }

    @MessageMapping("/project/{projectId}/enter")
    @SendTo("/topic/project/{projectId}/enter")
    public List<String> userEnter(@DestinationVariable int projectId, @Payload UserEnterRequest userEnterRequest, @Header("simpSessionAttributes") Map<String, Object> sessionAttr) {

        String project = "project:"+projectId;
        String username = "user/"+((String)sessionAttr.get("username"))+"/"+project;
        if(!userEnterRequest.isEnter()){
            redisService.deleteValue(username);
            redisService.deleteUserFromList(project,(String)sessionAttr.get("username"));
        }
        else if(redisService.getValue(username)==null) {
            redisService.setValue(username, "");
            redisService.addUserToList(project,(String)sessionAttr.get("username"));
        }
        return redisService.getOnlineUsers(project);
    }

}

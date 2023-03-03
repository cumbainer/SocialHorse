package ua.socialnetwork.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ua.socialnetwork.entity.Comment;
import ua.socialnetwork.entity.Friend;
import ua.socialnetwork.entity.Post;
import ua.socialnetwork.entity.User;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class UserSerializer extends StdSerializer<User> {

    public UserSerializer() {
        this(null);
    }

    public UserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(
            User user, JsonGenerator jgen, SerializerProvider provider)
            throws IOException{

        jgen.writeStartObject();

        jgen.writeStringField("username", user.getUsername());
        jgen.writeStringField("firstName", user.getFirstName());
        jgen.writeStringField("lastName", user.getLastName());
        jgen.writeStringField("email", user.getEmail());
        jgen.writeStringField("bio", user.getBio());
        jgen.writeNumberField("age", user.getAge());
        jgen.writeStringField("country", user.getCountry());
        jgen.writeStringField("gender", user.getGender().toString());
        jgen.writeBooleanField("isEmployed", user.isEmployed());

        jgen.writeArrayFieldStart("friends");
        for(Friend friend: user.getSentRequest()){
            jgen.writeStartObject();
            jgen.writeStringField("username", friend.getReceiver().getUsername());
            jgen.writeStringField("firstName", friend.getReceiver().getFirstName());
            jgen.writeStringField("lastName", friend.getReceiver().getLastName());
            jgen.writeEndObject();
        }
        for(Friend friend: user.getReceivedRequests()){
            jgen.writeStartObject();
            jgen.writeStringField("username", friend.getSender().getUsername());
            jgen.writeStringField("firstName", friend.getSender().getFirstName());
            jgen.writeStringField("lastName", friend.getSender().getLastName());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();

        jgen.writeArrayFieldStart("posts");
        for(Post post : user.getPosts()){
            jgen.writeStartObject();
            jgen.writeStringField("body", post.getBody());
            jgen.writeStringField("creationDate", post.getCreationDate());

            jgen.writeArrayFieldStart("comments");
            for(Comment comment : post.getComments()){
                jgen.writeStartObject();
                jgen.writeStringField("body", comment.getText());
                jgen.writeStringField("senderUsername", comment.getCreatedBy().getUsername());
                jgen.writeStringField("creationDate", comment.getCreatedDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                jgen.writeEndObject();
            }
            jgen.writeEndArray();
            jgen.writeEndObject();
        }
        jgen.writeEndArray();

        jgen.writeEndObject();
    }
}
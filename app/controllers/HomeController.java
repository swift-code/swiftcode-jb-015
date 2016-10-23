package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ConnectionRequest;
import models.Profile;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.stream.Collectors;

/**
 * Created by lubuntu on 10/23/16.
 */
public class HomeController extends Controller {
@Inject
    ObjectMapper objectMapper;

    public Result getProfile(Long id) {
        ObjectNode data = objectMapper.createObjectNode();

        User user = User.find.byId(id);
        Profile profile = Profile.find.byId(user.profile.id);
        data.put("id", user.id);
        data.put("email", user.email);
        data.put("firstName", profile.firstName);
        data.put("lastName", profile.lastName);
        data.put("company", profile.company);

        //first lambda

        data.set("connections", objectMapper.valueToTree(user.connections.stream().map(connection -> {
            ObjectNode connectionJson = objectMapper.createObjectNode();
            User connectionUser = User.find.byId(connection.id);
            Profile connectionProfile = Profile.find.byId(connection.profile.id);

            connectionJson.put("id", connectionUser.id);
            connectionJson.put("email", connectionUser.email);
            connectionJson.put("firstName", connectionProfile.firstName);
            connectionJson.put("lastName", connectionProfile.lastName);
            connectionJson.put("company", connectionProfile.company);
            return connectionJson;
        }).collect(Collectors.toList())));


        //second lambda

        data.set("connectionRequestsReceived", objectMapper.valueToTree(user.connectionRequestsReceived.stream().filter(x -> x.status.equals(ConnectionRequest.Status.WAITING)).map(x -> {
            ObjectNode connectionRequest = objectMapper.createObjectNode();
            Profile senderProfile = Profile.find.byId(x.sender.profile.id);
            connectionRequest.put("id", x.id);
            connectionRequest.put("firstName", senderProfile.firstName);
            return connectionRequest;
        }).collect(Collectors.toList())));

        //third lambda

        data.set("connectionRequestsReceived", objectMapper.valueToTree(
        User.find.all().stream()
                .filter(x -> !user.equals(x))
                .filter(x -> !user.connections.contains(x))
                .filter(x -> !user.connectionRequestsReceived.stream().map(y -> y.sender).collect(Collectors.toList())
                        .contains(x)).
                filter(x -> !user.connectionRequestsSent.stream().map(y -> y.receiver).collect(Collectors.toList())
                        .contains(x))
                .map(suggestion -> {

                    ObjectNode suggestionJson = objectMapper.createObjectNode();
                    Profile suggestionProfile = Profile.find.byId(suggestion.profile.id);
                    suggestionJson.put("id", suggestion.id);
                    suggestionJson.put("firstName", suggestionProfile.firstName);
                    suggestionJson.put("lastName", suggestionProfile.lastName);
                    return suggestionJson;

                }).collect(Collectors.toList())));


        return ok(data);

    }




}
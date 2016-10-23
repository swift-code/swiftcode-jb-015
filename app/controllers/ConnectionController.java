package controllers;

import models.ConnectionRequest;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;


/**
 * Created by lubuntu on 10/23/16.
 */
public class ConnectionController extends Controller {
    public Result sendRequest(Long senderid, Long receiverid)
    {
       if(senderid==null || receiverid==null || User.find.byId(senderid)==null || User.find.byId(receiverid)==null) {
           return ok();
       }
        ConnectionRequest c= new ConnectionRequest();
        c.receiver=User.find.byId(receiverid);
        c.sender=User.find.byId(senderid);
        c.status=ConnectionRequest.Status.WAITING;
        ConnectionRequest.db().save(c);
        return ok();

    }
    public Result acceptRequest(Long receiverid)
    {
        if(receiverid!=null)
            return  ok();
        ConnectionRequest c = ConnectionRequest.find.byId(receiverid);
        c.status=ConnectionRequest.Status.ACCEPTED;

        User sender = User.find.byId(c.sender.id);
        User receiver = User.find.byId(c.receiver.id);

        sender.connections.add(c.receiver);
        receiver.connections.add(c.sender);

        ConnectionRequest.db().update(c);
        c.sender.update();
        c.receiver.update();
        User.db().update(c.sender);
        User.db().update(c.receiver);
        return ok();


    }
}

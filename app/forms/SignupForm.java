package forms;

import com.sun.jna.platform.win32.Netapi32Util;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import views.html.defaultpages.error;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lubuntu on 10/22/16.
 */
public class SignupForm {
    @Constraints.Required
    public String email;

    @Constraints.Required
    public String password;

    @Constraints.Required
    public String firstname;

    public String lastname;

    public List<ValidationError> validate(){

        List<ValidationError> errors = new ArrayList<>();
        User user = User.find.where().eq("email",email).findUnique();

        if(user != null){
            errors.add(new ValidationError("message","This email ID is already registered"));
        }
        return errors;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}

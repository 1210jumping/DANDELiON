package espresso;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.test.espresso.Root;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.myeducationapp.R;
import com.example.myeducationapp.ui.login.LoginActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
/**
 * @author Ethan Yifan Zhu
 * UI test for Login page
 * **/
@RunWith(AndroidJUnit4.class)

@LargeTest
public class LoginTest {
    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);
    public IntentsTestRule<LoginActivity> intentsTestRule = new IntentsTestRule<>(LoginActivity.class);

    /**
    * This test is to check whether all elements are displayed properly
    * **/
    @Test
    public void checkDisplayedProperly(){
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
        onView(withId(R.id.login)).check(matches(isDisplayed()));
        onView(withId(R.id.register)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView2)).check(matches(isDisplayed()));
        onView(withId(R.id.Terms)).check(matches(isDisplayed()));
        onView(withId(R.id.Privacy)).check(matches(isDisplayed()));
    }

    /**
    * This test is to check whether login button is not enabled.
    * If there is no texts in both "username" and "password",
    * then the button should be disabled.
    * **/
    @Test
    public void checkLoginButtonNotEnabled(){
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.login)).check(matches(not(isEnabled())));

        onView(withId(R.id.username)).perform(typeText("123@1.1"));
        onView(withId(R.id.login)).check(matches(not(isEnabled())));
    }

    /**
     * This test is to check if button enabled successfully.
     * After enter texts in both area,
     * the button should be enabled.
     * **/
    @Test
    public void checkLoginButtonEnabled(){
        onView(withId(R.id.login)).check(matches(not(isEnabled())));
        onView(withId(R.id.username)).perform(typeText("123@1.1"));
        onView(withId(R.id.password)).perform(typeText("123456"));
        onView(withId(R.id.login)).check(matches(isEnabled()));
    }

    /**
     * Check the function of register button
     * **/
    @Test
    public void checkRegister(){
        onView(withId(R.id.register)).perform(click());
        onView(withText("Name")).check(matches(isDisplayed()));
    }
    /**
     * When username is empty, there should be an error appear in the text box
     * **/
    @Test
    public void emailIsEmpty(){
        onView(withId(R.id.username)).perform(typeText("111"));
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.username)).check(matches(withError("Not a valid username")));
    }

    /**
     * Email is not well formed
     * **/
    @Test
    public void emailWellFormed(){
        onView(withId(R.id.username)).perform(typeText("123@"));
        onView(withId(R.id.username)).check(matches(withError("Not a valid username")));

        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.username)).perform(typeText("123@1."));
        onView(withId(R.id.username)).check(matches(withError("Not a valid username")));
    }
    /**
     * When password is empty, there should be an error appear in the text box
     * **/
    @Test
    public void pwdIsEmpty(){
        onView(withId(R.id.password)).perform(typeText("111"));
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.password)).check(matches(withError("Password must be >5 characters")));
    }
    /**
     * When entering the wrong password,
     * there appears a pop-up window.
     * **/
    @Test
    public void wrongPwdMatch() throws InterruptedException {
        onView(withId(R.id.username)).perform(typeText("123@1.1"));
        onView(withId(R.id.password)).perform(typeText("1234567890"));
        closeSoftKeyboard();
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(5000);
        onView(withText("Alert")).check(matches(isDisplayed()));
    }

    /**
     * When entering the right password,
     * login successfully.
     * **/
    @Test
    public void rightPwdMatch() throws InterruptedException {
        onView(withId(R.id.username)).perform(typeText("123@1.1"));
        onView(withId(R.id.password)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(5000);
        onView(withText("Calender")).check(matches(isDisplayed()));
    }

    private static Matcher<View> withError(final String expected){
        return new TypeSafeMatcher<View>(){
            @Override
            public void describeTo(Description description) {
                description.appendText("Not found error message" + expected + ", find it!");
            }

            @Override
            protected boolean matchesSafely(View item){
                if(item instanceof EditText){
                    return ((EditText)item).getError().toString().equals(expected);
                }
                return false;
            }
        };
    }

    public static class RegisterActivityTest {

    }
}

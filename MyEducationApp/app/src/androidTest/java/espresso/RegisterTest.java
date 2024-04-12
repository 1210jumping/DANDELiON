package espresso;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import android.view.View;
import android.widget.EditText;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.myeducationapp.R;
import com.example.myeducationapp.ui.register.RegisterActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
/**
 * @author u7560434 Ethan Yifan Zhu
 * UI test for Register page
 * **/
@RunWith(AndroidJUnit4.class)

@LargeTest
public class RegisterTest {
    @Rule
    public ActivityTestRule<RegisterActivity> activityTestRule = new ActivityTestRule<>(RegisterActivity.class);
    public IntentsTestRule<RegisterActivity> intentsTestRule = new IntentsTestRule<>(RegisterActivity.class);

    /**
     * This function is to check whether all elements are shown properly
     * **/
    @Test
    public void checkDisplayProperly(){
        onView(withId(R.id.name)).check(matches(isDisplayed()));
        onView(withId(R.id.textEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.textPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.TextName)).check(matches(isDisplayed()));
        onView(withId(R.id.email)).check(matches(isDisplayed()));
        onView(withId(R.id.password3)).check(matches(isDisplayed()));
        onView(withId(R.id.TermsCheck)).check(matches(isDisplayed()));
        onView(withId(R.id.email2)).check(matches(isDisplayed()));
        onView(withId(R.id.password2)).check(matches(isDisplayed()));
        onView(withId(R.id.joinnow)).check(matches(isDisplayed()));
        onView(withId(R.id.PrivacyCheck)).check(matches(isDisplayed()));
        onView(withId(R.id.registerLogo)).check(matches(isDisplayed()));
    }

    /**
     * This test is to check whether two checkbox are not checked and join button is disabled in default situation
     * **/
    @Test
    public void checkDefault(){
        onView(withId(R.id.TermsCheck)).check(matches(not(isChecked())));
        onView(withId(R.id.PrivacyCheck)).check(matches(not(isChecked())));
        onView(withId(R.id.joinnow)).check(matches(isNotEnabled()));
    }

    /**
     * Check join button being enabled
     * **/
    @Test
    public void checkButtonEnabled(){
        onView(withId(R.id.TermsCheck)).perform(click());
        onView(withId(R.id.PrivacyCheck)).perform(click());
        onView(withId(R.id.joinnow)).check(matches(isEnabled()));
    }
    /**
     * When name is null, there would be an error
     * **/
    @Test
    public void nameIsEmpty(){
        onView(withId(R.id.name)).perform(typeText("123"));
        onView(withId(R.id.name)).perform(clearText());
        onView(withId(R.id.name)).check(matches(withError("Name should not be blank")));
    }

    /**
     * When input is null, there would be an error
     * **/
    @Test
    public void inputIsEmpty(){
        // Name
        onView(withId(R.id.name)).perform(typeText("123"));
        onView(withId(R.id.name)).perform(clearText());
        onView(withId(R.id.name)).check(matches(withError("Name should not be blank")));
        closeSoftKeyboard();

        // Email
        onView(withId(R.id.email)).perform(typeText("123"));
        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.email)).check(matches(withError("Not a valid email")));
        closeSoftKeyboard();

        // Confirm Email
        onView(withId(R.id.email2)).perform(typeText("123"));
        onView(withId(R.id.email2)).perform(clearText());
        onView(withId(R.id.email2)).check(matches(withError("Not a valid email")));
        closeSoftKeyboard();

        // Password
        onView(withId(R.id.password2)).perform(typeText("123"));
        onView(withId(R.id.password2)).check(matches(withError("Password length should be greater than 5")));
        onView(withId(R.id.password2)).perform(clearText());
        onView(withId(R.id.password2)).check(matches(withError("Password length should be greater than 5")));
        closeSoftKeyboard();

        // Confirm password
        onView(withId(R.id.password3)).perform(typeText("123"));
        onView(withId(R.id.password3)).check(matches(withError("Password length should be greater than 5")));
        onView(withId(R.id.password3)).perform(clearText());
        onView(withId(R.id.password3)).check(matches(withError("Password length should be greater than 5")));
        closeSoftKeyboard();
    }

    /**
     * When input is null, there would be an error
     * **/
    @Test
    public void RegisterWithEmptyName(){
        onView(withId(R.id.TermsCheck)).perform(click());
        onView(withId(R.id.PrivacyCheck)).perform(click());
        onView(withId(R.id.joinnow)).perform(click());
        onView(withText("Username should not be empty")).check(matches(isDisplayed()));
    }

    /**
     * When input is null, there would be an error
     * **/
    @Test
    public void RegisterWithEmptyEmail(){
        onView(withId(R.id.name)).perform(typeText("AAA"));
        closeSoftKeyboard();
        onView(withId(R.id.TermsCheck)).perform(click());
        onView(withId(R.id.PrivacyCheck)).perform(click());
        onView(withId(R.id.joinnow)).perform(click());
        onView(withText("Email should not be empty")).check(matches(isDisplayed()));
    }

    /**
     * When input is inconsistent, there would be an error
     * **/
    @Test
    public void RegisterWithInconsistentEmail(){
        onView(withId(R.id.name)).perform(typeText("AAA"));
        closeSoftKeyboard();
        onView(withId(R.id.email)).perform(typeText("123@1.1"));
        closeSoftKeyboard();
        onView(withId(R.id.email2)).perform(typeText("1232@1.1"));
        closeSoftKeyboard();
        onView(withId(R.id.TermsCheck)).perform(click());
        onView(withId(R.id.PrivacyCheck)).perform(click());
        onView(withId(R.id.joinnow)).perform(click());
        onView(withText("Please check your email again")).check(matches(isDisplayed()));
    }

    /**
     * When input is null, there would be an error
     * **/
    @Test
    public void RegisterWithEmptyPwd(){
        onView(withId(R.id.name)).perform(typeText("AAA"));
        closeSoftKeyboard();
        onView(withId(R.id.email)).perform(typeText("123@1.1"));
        closeSoftKeyboard();
        onView(withId(R.id.email2)).perform(typeText("123@1.1"));
        closeSoftKeyboard();
        onView(withId(R.id.TermsCheck)).perform(click());
        onView(withId(R.id.PrivacyCheck)).perform(click());
        onView(withId(R.id.joinnow)).perform(click());
        onView(withText("Password should be longer than 5")).check(matches(isDisplayed()));
    }

    /**
     * When input is inconsistent, there would be an error
     * **/
    @Test
    public void RegisterWithInconsistentPwd(){
        onView(withId(R.id.name)).perform(typeText("AAA"));
        closeSoftKeyboard();
        onView(withId(R.id.email)).perform(typeText("123@1.1"));
        closeSoftKeyboard();
        onView(withId(R.id.email2)).perform(typeText("1232@1.1"));
        closeSoftKeyboard();
        onView(withId(R.id.TermsCheck)).perform(click());
        onView(withId(R.id.PrivacyCheck)).perform(click());
        onView(withId(R.id.joinnow)).perform(click());
        onView(withText("Please check your email again")).check(matches(isDisplayed()));
    }

    /**
     * When email is registered, there would be an error
     * **/
    @Test
    public void registerWithExistedEmail(){
        onView(withId(R.id.name)).perform(typeText("AAA"));
        closeSoftKeyboard();
        onView(withId(R.id.email)).perform(typeText("123@1.1"));
        closeSoftKeyboard();
        onView(withId(R.id.email2)).perform(typeText("123@1.1"));
        closeSoftKeyboard();
        onView(withId(R.id.password2)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.password3)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.TermsCheck)).perform(click());
        onView(withId(R.id.PrivacyCheck)).perform(click());
        onView(withId(R.id.joinnow)).perform(click());
        onView(withText("You have registered already with this email")).check(matches(isDisplayed()));
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
}

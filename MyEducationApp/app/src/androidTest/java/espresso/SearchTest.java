package espresso;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.myeducationapp.DAO.CourseDAO.CourseDao;
import com.example.myeducationapp.DAO.UserDAO.MyUser;
import com.example.myeducationapp.DAO.UserDAO.User;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.R;
import com.example.myeducationapp.ui.navigation.SearchActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
/**
 * @author u7560434 Ethan Yifan Zhu
 * UI test for search page
 * **/
@RunWith(AndroidJUnit4.class)

@LargeTest
public class SearchTest {
    @Rule
    public ActivityTestRule<SearchActivity> activityTestRule = new ActivityTestRule<>(SearchActivity.class);

    /**
     * Before test begin, initialize all the courses
     * **/
    @BeforeClass
    public static void init() throws InterruptedException {
        new CourseDao().findAllCourses();
        Thread.sleep(2000);
    }

    /**
     * To Check whether all elements are displayed properly
     * */
    @Test
    public void displayProperly(){
        onView(withId(R.id.searchText)).check(matches(isDisplayed()));
        onView(withId(R.id.courseList)).check(matches(isDisplayed()));
        onView(withId(R.id.searchBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.radioButton1)).check(matches(isDisplayed()));
        onView(withId(R.id.radioButton2)).check(matches(isDisplayed()));
        onView(withId(R.id.ascOrder)).check(matches(isDisplayed()));
        onView(withId(R.id.descOrder)).check(matches(isDisplayed()));
        onView(withId(R.id.sortText)).check(matches(isDisplayed()));
    }

    /**
     * Checkable test
     * **/
    @Test
    public void checkListViewClickable(){
        Global.currentUser=MyUser.getInstance(new User("zLNMIAyxlPPwxX0LvauvOnNP2CB2", "comp6442", "comp6442@anu.au",1, "comp6442",""));
        onView(withId(R.id.radioButton1)).perform(click());
        onData(instanceOf(Map.class)).inAdapterView(withId(R.id.courseList)).atPosition(0).perform(click());
    }

    /**
     * Checkable test
     * **/
    @Test
    public void checkRatioClickable(){
        onView(withId(R.id.radioButton1)).check(matches(isClickable()));
        onView(withId(R.id.radioButton1)).perform(click());
        onView(withId(R.id.radioButton2)).check(matches(isClickable()));
        onView(withId(R.id.radioButton2)).perform(click());
        onView(withId(R.id.radioButton1)).check(matches(isClickable()));
        onView(withId(R.id.radioButton1)).perform(click());
    }

    /**
     * Checkable test
     * **/
    @Test
    public void checkSortButton(){
        onView(withId(R.id.ascOrder)).check(matches(isClickable()));
        onView(withId(R.id.ascOrder)).perform(click());
        onView(withId(R.id.descOrder)).check(matches(isClickable()));
        onView(withId(R.id.descOrder)).perform(click());
        onView(withId(R.id.ascOrder)).check(matches(isClickable()));
        onView(withId(R.id.ascOrder)).perform(click());
    }

    /**
     * Wrong token test
     * **/
    @Test
    public void searchWithWrongToken(){
        onView(withId(R.id.searchText)).perform(typeText("1111"));
        closeSoftKeyboard();
        onView(withId(R.id.searchBtn)).perform(click());
        onView(withText(R.string.no_course_found));
    }

    /**
     * Right token test
     * **/
    @Test
    public void searchWithRightToken(){
        onView(withId(R.id.searchText)).perform(typeText("CNAME=Statistics"));
        closeSoftKeyboard();
        onView(withId(R.id.searchBtn)).perform(click());
        onView(withText("Stephanie Davis")).check(matches(isDisplayed()));

        onView(withId(R.id.searchText)).perform(typeText("SUB=Computer Science;LEC=Evelyn Parker"));
        closeSoftKeyboard();
        onView(withId(R.id.searchBtn)).perform(click());
        onView(withText("Physics")).check(matches(isDisplayed()));
    }

    /**
     * Fuzzy search test
     * **/
    @Test
    public void FuzzySearchTest(){
        onView(withId(R.id.searchText)).perform(typeText("CNO=CO"));
        closeSoftKeyboard();
        onView(withId(R.id.searchBtn)).perform(click());
        onView(withText("Music Production")).check(matches(isDisplayed()));

        onView(withId(R.id.searchText)).perform(typeText("CNAME=Phy"));
        closeSoftKeyboard();
        onView(withId(R.id.searchBtn)).perform(click());
        onView(withText("Physics")).check(matches(isDisplayed()));
    }

}

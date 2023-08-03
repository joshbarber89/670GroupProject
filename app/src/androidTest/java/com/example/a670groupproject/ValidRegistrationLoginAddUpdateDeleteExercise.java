package com.example.a670groupproject;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ValidRegistrationLoginAddUpdateDeleteExercise {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void validRegistrationLoginAddUpdateDeleteExercise() {

        String randomEmailAddress = getRandomAlphabetString() + "@domain.com";

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.loginActivityButtonRegister), withText("register"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.registerActivityEmailAddress),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText(randomEmailAddress), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.registerActivityPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("pass123"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.registerActivityRePassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("pass123"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.registerActivityButtonRegister), withText("Register Account"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.loginActivityEditTextEmailAddress),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText(randomEmailAddress), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.loginActivityEditTextPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("pass123"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.loginActivityButtonLogin), withText("login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.exerciseButton), withText("Exercise"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.addEntryButton), withText("Add New Entry"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.exerciseReading),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("Running 10 mins"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.exerciseReading), withText("Running 10 mins"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatEditText7.perform(pressImeActionButton());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.submitexerciseButton), withText("Add New Entry"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.entryValue), withText("Running 10 mins"),
                        withParent(withParent(withId(R.id.exerciseList))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.exerciseList),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)))
                .atPosition(0);
        linearLayout.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.exerciseReading), withText("Running 10 mins"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("Running 20 mins"));

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.exerciseReading), withText("Running 20 mins"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatEditText9.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.exerciseReading), withText("Running 20 mins"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatEditText10.perform(pressImeActionButton());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.updateexerciseButton), withText("Update Entry"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.entryValue), withText("Running 20 mins"),
                        withParent(withParent(withId(R.id.exerciseList))),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        DataInteraction linearLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.exerciseList),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)))
                .atPosition(0);
        linearLayout2.perform(click());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.deleteexerciseButton), withText("Delete Entry"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1),
                        isDisplayed()));
        materialButton8.perform(click());

        ViewInteraction relativeLayout = onView(
                allOf(withParent(withParent(withId(R.id.flContainer))),
                        isDisplayed()));
        relativeLayout.check(matches(isDisplayed()));
    }

    private String getRandomAlphabetString() {
        String random = "";

        for(int i = 0; i < 8; i++){
            int randomNumber = (int)((Math.random() * 2) + 1);
            if (randomNumber == 1) {
                int randomNumberForUppercaseCharacter = (int) ((Math.random() * 26) + 65);
                char ch = (char)randomNumberForUppercaseCharacter;
                random += ch;
            }
            else{
                int randomNumberForLowercaseCharacter = (int) ((Math.random() * 26) + 97);
                char ch = (char)randomNumberForLowercaseCharacter;
                random += ch;
            }
        }

        return random;
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

package com.example.a670groupproject;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

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
public class InvalidLogin {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void invalidLogin() {

        String randomEmailAddress = getRandomAlphabetString() + "@domain.com";

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.loginActivityEditTextEmailAddress),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText(randomEmailAddress), closeSoftKeyboard());

        String randomPassword = getRandomAlphabetString();

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.loginActivityEditTextPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText(randomPassword), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.loginActivityButtonLogin), withText("login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withText("Login"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withText("Password"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        ViewInteraction editText = onView(
                allOf(withId(R.id.loginActivityEditTextEmailAddress), withText(randomEmailAddress),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText.check(matches(isDisplayed()));
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

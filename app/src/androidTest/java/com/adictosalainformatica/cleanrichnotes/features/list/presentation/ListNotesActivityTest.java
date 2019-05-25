package com.adictosalainformatica.cleanrichnotes.features.list.presentation;

import android.content.ComponentName;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.adictosalainformatica.cleanrichnotes.R;
import com.adictosalainformatica.cleanrichnotes.features.add.presentation.EditNote;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ListNotesActivityTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(ListNotesActivity.class);

    @Before
    public void before() {
        Intents.init();
    }


    @Test
    public void test_that_edit_note_activity_is_shown_when_add_fab_is_clicked() {
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click());
        intended(hasComponent(new ComponentName(getTargetContext(), EditNote.class)));
        Intents.release();
    }
}
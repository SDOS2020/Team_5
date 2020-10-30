package org.emberon.winscan.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import org.emberon.winscan.domain.entity.User;
import org.emberon.winscan.injection.ApplicationContext;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocalRepository {

    private static final String USER = "preferences_user";

    private final JsonSerializer<Date> dateJsonSerializer =
            (src, typeOfSrc, context) -> src == null ? null
                    : new JsonPrimitive(src.getTime());
    private final JsonDeserializer<Date> dateJsonDeserializer =
            (jSon, typeOfT, context) -> jSon == null ? null : new Date(jSon.getAsLong());
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, dateJsonSerializer)
            .registerTypeAdapter(Date.class, dateJsonDeserializer).create();
    private SharedPreferences sharedPreferences;

    @Inject
    public LocalRepository(@ApplicationContext Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public User getUser() {
        String json = getString(USER, "");
        if (json == null || json.isEmpty()) {
            return null;
        }
        return gson.fromJson(json, User.class);
    }

    public void saveUser(User user) {
        String json = gson.toJson(user);
        putString(USER, json);
    }

    // Preferences Helper Methods
    private void remove(String preferenceKey) {
        sharedPreferences.edit().remove(preferenceKey).apply();
    }

    private int getInt(String preferenceKey, int preferenceDefaultValue) {
        return sharedPreferences.getInt(preferenceKey, preferenceDefaultValue);
    }

    private void putInt(String preferenceKey, int preferenceValue) {
        sharedPreferences.edit().putInt(preferenceKey, preferenceValue).apply();
    }

    private long getLong(String preferenceKey, long preferenceDefaultValue) {
        return sharedPreferences.getLong(preferenceKey, preferenceDefaultValue);
    }

    private void putLong(String preferenceKey, long preferenceValue) {
        sharedPreferences.edit().putLong(preferenceKey, preferenceValue).apply();
    }

    private String getString(String preferenceKey, String preferenceDefaultValue) {
        return sharedPreferences.getString(preferenceKey, preferenceDefaultValue);
    }

    private void putString(String preferenceKey, String preferenceValue) {
        sharedPreferences.edit().putString(preferenceKey, preferenceValue).apply();
    }

    private boolean getBoolean(String preferenceKey, boolean preferenceDefaultValue) {
        return sharedPreferences.getBoolean(preferenceKey, preferenceDefaultValue);
    }

    private void putBoolean(String preferenceKey, boolean preferenceValue) {
        sharedPreferences.edit().putBoolean(preferenceKey, preferenceValue).apply();
    }
}


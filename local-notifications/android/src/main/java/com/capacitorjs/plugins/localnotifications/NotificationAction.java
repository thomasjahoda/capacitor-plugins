package com.capacitorjs.plugins.localnotifications;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Action types that will be registered for the notifications
 */
public class NotificationAction {

    private String id;
    private String title;
    private Boolean input;
    private Boolean openApp;
    private Boolean dismissNotification;

    public NotificationAction() {}

    public NotificationAction(String id, String title, Boolean input, Boolean openApp, Boolean dismissNotification) {
        this.id = id;
        this.title = title;
        this.input = input;
        this.openApp = openApp;
        this.dismissNotification = dismissNotification;
    }

    public static Map<String, NotificationAction[]> buildTypes(JSArray types) {
        Map<String, NotificationAction[]> actionTypeMap = new HashMap<>();
        try {
            List<JSONObject> objects = types.toList();
            for (JSONObject obj : objects) {
                JSObject jsObject = JSObject.fromJSONObject(obj);
                String actionGroupId = jsObject.getString("id");
                if (actionGroupId == null) {
                    return null;
                }
                JSONArray actions = jsObject.getJSONArray("actions");
                if (actions != null) {
                    NotificationAction[] typesArray = new NotificationAction[actions.length()];
                    for (int i = 0; i < typesArray.length; i++) {
                        NotificationAction notificationAction = new NotificationAction();
                        JSObject action = JSObject.fromJSONObject(actions.getJSONObject(i));
                        notificationAction.setId(action.getString("id"));
                        notificationAction.setTitle(action.getString("title"));
                        notificationAction.setInput(action.getBool("input"));
                        notificationAction.setOpenApp(action.getBool("openApp"));
                        notificationAction.setDismissNotification(action.getBool("dismissNotification"));
                        typesArray[i] = notificationAction;
                    }
                    actionTypeMap.put(actionGroupId, typesArray);
                }
            }
        } catch (Exception e) {
            Logger.error(Logger.tags("LN"), "Error when building action types", e);
        }
        return actionTypeMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isInput() {
        return Boolean.TRUE.equals(input);
    }

    public void setInput(Boolean input) {
        this.input = input;
    }

    public boolean isOpenApp() {
        // default to true
        return !Boolean.FALSE.equals(openApp);
    }

    public void setOpenApp(Boolean openApp) {
        this.openApp = openApp;
    }

    public boolean isDismissNotification() {
        // default to true
        return !Boolean.FALSE.equals(dismissNotification);
    }

    public void setDismissNotification(Boolean dismissNotification) {
        this.dismissNotification = dismissNotification;
    }
}

package com.haulmont.teamcity;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.notification.NotificatorAdapter;
import jetbrains.buildServer.notification.NotificatorRegistry;
import jetbrains.buildServer.serverSide.SRunningBuild;
import jetbrains.buildServer.serverSide.UserPropertyInfo;
import jetbrains.buildServer.users.NotificatorPropertyKey;
import jetbrains.buildServer.users.SUser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * @author galimov
 * @version $Id$
 */
public class SlackNotificator extends NotificatorAdapter {

    private Logger log = Logger.getInstance(NotificatorAdapter.class.getName());

    public final static String SLACK_CHANNEL_PROPERTY = "slack.Channel";
    public final static String SLACK_USERNAME_PROPERTY = "slack.Username";
    public final static String SLACK_WEBHOOK_PROPERTY = "slack.Webhook";

    private UserPropertyInfo[] properties = new UserPropertyInfo[] {
            new UserPropertyInfo(SLACK_CHANNEL_PROPERTY, "Channel"),
            new UserPropertyInfo(SLACK_USERNAME_PROPERTY, "Username"),
            new UserPropertyInfo(SLACK_WEBHOOK_PROPERTY, "Webhook URL"),
    };

    public SlackNotificator(NotificatorRegistry notificatorRegistry) {
        notificatorRegistry.register(this, Arrays.asList(properties));
    }

    @NotNull
    @Override
    public String getNotificatorType() {
        return "SlackNotificator";
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Slack Notifier";
    }

    @Override
    public void notifyBuildSuccessful(@NotNull SRunningBuild build, @NotNull Set<SUser> users) {
        log.info("build successful " + build.getBuildTypeId());
        sendMessage("build successful", users);
    }

    private void sendMessage(String text, Set<SUser> users) {
        for(SUser user : users) {
            String channel = user.getPropertyValue(new NotificatorPropertyKey(getNotificatorType(), SLACK_CHANNEL_PROPERTY));
            String username = user.getPropertyValue(new NotificatorPropertyKey(getNotificatorType(), SLACK_USERNAME_PROPERTY));
            String webhook = user.getPropertyValue(new NotificatorPropertyKey(getNotificatorType(), SLACK_WEBHOOK_PROPERTY));
            Payload payload = Payload.builder()
                    .channel(channel)
                    .username(username)
                    .text(text)
                    .build();

            Slack slack = Slack.getInstance();
            try {
                slack.send(webhook, payload);
            } catch (IOException e) {
                log.error("Error on sending message", e);
            }
        }
    }

}

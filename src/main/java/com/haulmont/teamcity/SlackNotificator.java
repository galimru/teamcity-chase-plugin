package com.haulmont.teamcity;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.notification.NotificatorAdapter;
import jetbrains.buildServer.notification.NotificatorRegistry;
import jetbrains.buildServer.serverSide.SRunningBuild;
import jetbrains.buildServer.serverSide.UserPropertyInfo;
import jetbrains.buildServer.users.SUser;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;

/**
 * @author galimov
 * @version $Id$
 */
public class SlackNotificator extends NotificatorAdapter {

    Logger log = Logger.getInstance(NotificatorAdapter.class.getName());

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
        log.info("build successful" + build.getBuildTypeId());
    }

}

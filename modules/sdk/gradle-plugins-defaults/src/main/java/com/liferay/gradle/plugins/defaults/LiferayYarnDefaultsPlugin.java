/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.gradle.plugins.defaults;

import com.liferay.gradle.plugins.NodeDefaultsPlugin;
import com.liferay.gradle.plugins.defaults.internal.LiferayCIPatcherPlugin;
import com.liferay.gradle.plugins.defaults.internal.LiferayCIPlugin;
import com.liferay.gradle.plugins.defaults.internal.util.GradleUtil;
import com.liferay.gradle.plugins.node.YarnPlugin;
import com.liferay.gradle.plugins.node.tasks.YarnInstallTask;
import com.liferay.gradle.util.Validator;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;

/**
 * @author Peter Shin
 */
public class LiferayYarnDefaultsPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		GradleUtil.applyPlugin(project, YarnPlugin.class);

		GradleUtil.applyPlugin(project, NodeDefaultsPlugin.class);

		com.liferay.gradle.plugins.defaults.internal.NodeDefaultsPlugin.
			INSTANCE.apply(project);

		if (_isRunningInCIEnvironment()) {
			LiferayCIPlugin.INSTANCE.apply(project);
		}

		if (_isRunningInCIPatcherEnvironment()) {
			LiferayCIPatcherPlugin.INSTANCE.apply(project);
		}

		TaskProvider<YarnInstallTask> yarnInstallTaskProvider =
			GradleUtil.getTaskProvider(
				project, YarnPlugin.YARN_INSTALL_TASK_NAME,
				YarnInstallTask.class);

		_configureTaskYarnInstallProvider(yarnInstallTaskProvider);
	}

	private void _configureTaskYarnInstallProvider(
		TaskProvider<YarnInstallTask> yarnInstallTaskProvider) {

		yarnInstallTaskProvider.configure(
			new Action<YarnInstallTask>() {

				@Override
				public void execute(YarnInstallTask yarnInstallTask) {
					String buildProfile = System.getProperty("build.profile");

					if (Validator.isNotNull(buildProfile)) {
						yarnInstallTask.setEnabled(false);
					}
				}

			});
	}

	private boolean _isRunningInCIEnvironment() {
		if (Validator.isNotNull(System.getenv("JENKINS_HOME"))) {
			return true;
		}

		return false;
	}

	private boolean _isRunningInCIPatcherEnvironment() {
		if (Validator.isNotNull(
				System.getenv("FIX_PACKS_RELEASE_ENVIRONMENT"))) {

			return true;
		}

		return false;
	}

}
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

package com.liferay.style.book.model;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link StyleBookEntryVersion}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see StyleBookEntryVersion
 * @generated
 */
public class StyleBookEntryVersionWrapper
	extends BaseModelWrapper<StyleBookEntryVersion>
	implements ModelWrapper<StyleBookEntryVersion>, StyleBookEntryVersion {

	public StyleBookEntryVersionWrapper(
		StyleBookEntryVersion styleBookEntryVersion) {

		super(styleBookEntryVersion);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("styleBookEntryVersionId", getStyleBookEntryVersionId());
		attributes.put("version", getVersion());
		attributes.put("styleBookEntryId", getStyleBookEntryId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("defaultStyleBookEntry", isDefaultStyleBookEntry());
		attributes.put("name", getName());
		attributes.put("previewFileEntryId", getPreviewFileEntryId());
		attributes.put("styleBookEntryKey", getStyleBookEntryKey());
		attributes.put("tokensValues", getTokensValues());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long styleBookEntryVersionId = (Long)attributes.get(
			"styleBookEntryVersionId");

		if (styleBookEntryVersionId != null) {
			setStyleBookEntryVersionId(styleBookEntryVersionId);
		}

		Integer version = (Integer)attributes.get("version");

		if (version != null) {
			setVersion(version);
		}

		Long styleBookEntryId = (Long)attributes.get("styleBookEntryId");

		if (styleBookEntryId != null) {
			setStyleBookEntryId(styleBookEntryId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Boolean defaultStyleBookEntry = (Boolean)attributes.get(
			"defaultStyleBookEntry");

		if (defaultStyleBookEntry != null) {
			setDefaultStyleBookEntry(defaultStyleBookEntry);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		Long previewFileEntryId = (Long)attributes.get("previewFileEntryId");

		if (previewFileEntryId != null) {
			setPreviewFileEntryId(previewFileEntryId);
		}

		String styleBookEntryKey = (String)attributes.get("styleBookEntryKey");

		if (styleBookEntryKey != null) {
			setStyleBookEntryKey(styleBookEntryKey);
		}

		String tokensValues = (String)attributes.get("tokensValues");

		if (tokensValues != null) {
			setTokensValues(tokensValues);
		}
	}

	/**
	 * Returns the company ID of this style book entry version.
	 *
	 * @return the company ID of this style book entry version
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the create date of this style book entry version.
	 *
	 * @return the create date of this style book entry version
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the default style book entry of this style book entry version.
	 *
	 * @return the default style book entry of this style book entry version
	 */
	@Override
	public boolean getDefaultStyleBookEntry() {
		return model.getDefaultStyleBookEntry();
	}

	/**
	 * Returns the group ID of this style book entry version.
	 *
	 * @return the group ID of this style book entry version
	 */
	@Override
	public long getGroupId() {
		return model.getGroupId();
	}

	/**
	 * Returns the name of this style book entry version.
	 *
	 * @return the name of this style book entry version
	 */
	@Override
	public String getName() {
		return model.getName();
	}

	/**
	 * Returns the preview file entry ID of this style book entry version.
	 *
	 * @return the preview file entry ID of this style book entry version
	 */
	@Override
	public long getPreviewFileEntryId() {
		return model.getPreviewFileEntryId();
	}

	/**
	 * Returns the primary key of this style book entry version.
	 *
	 * @return the primary key of this style book entry version
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the style book entry ID of this style book entry version.
	 *
	 * @return the style book entry ID of this style book entry version
	 */
	@Override
	public long getStyleBookEntryId() {
		return model.getStyleBookEntryId();
	}

	/**
	 * Returns the style book entry key of this style book entry version.
	 *
	 * @return the style book entry key of this style book entry version
	 */
	@Override
	public String getStyleBookEntryKey() {
		return model.getStyleBookEntryKey();
	}

	/**
	 * Returns the style book entry version ID of this style book entry version.
	 *
	 * @return the style book entry version ID of this style book entry version
	 */
	@Override
	public long getStyleBookEntryVersionId() {
		return model.getStyleBookEntryVersionId();
	}

	/**
	 * Returns the tokens values of this style book entry version.
	 *
	 * @return the tokens values of this style book entry version
	 */
	@Override
	public String getTokensValues() {
		return model.getTokensValues();
	}

	/**
	 * Returns the user ID of this style book entry version.
	 *
	 * @return the user ID of this style book entry version
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user name of this style book entry version.
	 *
	 * @return the user name of this style book entry version
	 */
	@Override
	public String getUserName() {
		return model.getUserName();
	}

	/**
	 * Returns the user uuid of this style book entry version.
	 *
	 * @return the user uuid of this style book entry version
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	/**
	 * Returns the version of this style book entry version.
	 *
	 * @return the version of this style book entry version
	 */
	@Override
	public int getVersion() {
		return model.getVersion();
	}

	/**
	 * Returns <code>true</code> if this style book entry version is default style book entry.
	 *
	 * @return <code>true</code> if this style book entry version is default style book entry; <code>false</code> otherwise
	 */
	@Override
	public boolean isDefaultStyleBookEntry() {
		return model.isDefaultStyleBookEntry();
	}

	/**
	 * Sets the company ID of this style book entry version.
	 *
	 * @param companyId the company ID of this style book entry version
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this style book entry version.
	 *
	 * @param createDate the create date of this style book entry version
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets whether this style book entry version is default style book entry.
	 *
	 * @param defaultStyleBookEntry the default style book entry of this style book entry version
	 */
	@Override
	public void setDefaultStyleBookEntry(boolean defaultStyleBookEntry) {
		model.setDefaultStyleBookEntry(defaultStyleBookEntry);
	}

	/**
	 * Sets the group ID of this style book entry version.
	 *
	 * @param groupId the group ID of this style book entry version
	 */
	@Override
	public void setGroupId(long groupId) {
		model.setGroupId(groupId);
	}

	/**
	 * Sets the name of this style book entry version.
	 *
	 * @param name the name of this style book entry version
	 */
	@Override
	public void setName(String name) {
		model.setName(name);
	}

	/**
	 * Sets the preview file entry ID of this style book entry version.
	 *
	 * @param previewFileEntryId the preview file entry ID of this style book entry version
	 */
	@Override
	public void setPreviewFileEntryId(long previewFileEntryId) {
		model.setPreviewFileEntryId(previewFileEntryId);
	}

	/**
	 * Sets the primary key of this style book entry version.
	 *
	 * @param primaryKey the primary key of this style book entry version
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the style book entry ID of this style book entry version.
	 *
	 * @param styleBookEntryId the style book entry ID of this style book entry version
	 */
	@Override
	public void setStyleBookEntryId(long styleBookEntryId) {
		model.setStyleBookEntryId(styleBookEntryId);
	}

	/**
	 * Sets the style book entry key of this style book entry version.
	 *
	 * @param styleBookEntryKey the style book entry key of this style book entry version
	 */
	@Override
	public void setStyleBookEntryKey(String styleBookEntryKey) {
		model.setStyleBookEntryKey(styleBookEntryKey);
	}

	/**
	 * Sets the style book entry version ID of this style book entry version.
	 *
	 * @param styleBookEntryVersionId the style book entry version ID of this style book entry version
	 */
	@Override
	public void setStyleBookEntryVersionId(long styleBookEntryVersionId) {
		model.setStyleBookEntryVersionId(styleBookEntryVersionId);
	}

	/**
	 * Sets the tokens values of this style book entry version.
	 *
	 * @param tokensValues the tokens values of this style book entry version
	 */
	@Override
	public void setTokensValues(String tokensValues) {
		model.setTokensValues(tokensValues);
	}

	/**
	 * Sets the user ID of this style book entry version.
	 *
	 * @param userId the user ID of this style book entry version
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user name of this style book entry version.
	 *
	 * @param userName the user name of this style book entry version
	 */
	@Override
	public void setUserName(String userName) {
		model.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this style book entry version.
	 *
	 * @param userUuid the user uuid of this style book entry version
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	/**
	 * Sets the version of this style book entry version.
	 *
	 * @param version the version of this style book entry version
	 */
	@Override
	public void setVersion(int version) {
		model.setVersion(version);
	}

	@Override
	public long getVersionedModelId() {
		return model.getVersionedModelId();
	}

	@Override
	public void setVersionedModelId(long id) {
		model.setVersionedModelId(id);
	}

	@Override
	public void populateVersionedModel(StyleBookEntry styleBookEntry) {
		model.populateVersionedModel(styleBookEntry);
	}

	@Override
	public StyleBookEntry toVersionedModel() {
		return model.toVersionedModel();
	}

	@Override
	protected StyleBookEntryVersionWrapper wrap(
		StyleBookEntryVersion styleBookEntryVersion) {

		return new StyleBookEntryVersionWrapper(styleBookEntryVersion);
	}

}
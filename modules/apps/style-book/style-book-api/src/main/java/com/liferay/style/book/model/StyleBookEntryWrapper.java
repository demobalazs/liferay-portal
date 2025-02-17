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
 * This class is a wrapper for {@link StyleBookEntry}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see StyleBookEntry
 * @generated
 */
public class StyleBookEntryWrapper
	extends BaseModelWrapper<StyleBookEntry>
	implements ModelWrapper<StyleBookEntry>, StyleBookEntry {

	public StyleBookEntryWrapper(StyleBookEntry styleBookEntry) {
		super(styleBookEntry);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("headId", getHeadId());
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
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		Long headId = (Long)attributes.get("headId");

		if (headId != null) {
			setHeadId(headId);
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
	 * Returns the company ID of this style book entry.
	 *
	 * @return the company ID of this style book entry
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the create date of this style book entry.
	 *
	 * @return the create date of this style book entry
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the default style book entry of this style book entry.
	 *
	 * @return the default style book entry of this style book entry
	 */
	@Override
	public boolean getDefaultStyleBookEntry() {
		return model.getDefaultStyleBookEntry();
	}

	/**
	 * Returns the group ID of this style book entry.
	 *
	 * @return the group ID of this style book entry
	 */
	@Override
	public long getGroupId() {
		return model.getGroupId();
	}

	/**
	 * Returns the head ID of this style book entry.
	 *
	 * @return the head ID of this style book entry
	 */
	@Override
	public long getHeadId() {
		return model.getHeadId();
	}

	@Override
	public String getImagePreviewURL(
		com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay) {

		return model.getImagePreviewURL(themeDisplay);
	}

	/**
	 * Returns the mvcc version of this style book entry.
	 *
	 * @return the mvcc version of this style book entry
	 */
	@Override
	public long getMvccVersion() {
		return model.getMvccVersion();
	}

	/**
	 * Returns the name of this style book entry.
	 *
	 * @return the name of this style book entry
	 */
	@Override
	public String getName() {
		return model.getName();
	}

	/**
	 * Returns the preview file entry ID of this style book entry.
	 *
	 * @return the preview file entry ID of this style book entry
	 */
	@Override
	public long getPreviewFileEntryId() {
		return model.getPreviewFileEntryId();
	}

	/**
	 * Returns the primary key of this style book entry.
	 *
	 * @return the primary key of this style book entry
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the style book entry ID of this style book entry.
	 *
	 * @return the style book entry ID of this style book entry
	 */
	@Override
	public long getStyleBookEntryId() {
		return model.getStyleBookEntryId();
	}

	/**
	 * Returns the style book entry key of this style book entry.
	 *
	 * @return the style book entry key of this style book entry
	 */
	@Override
	public String getStyleBookEntryKey() {
		return model.getStyleBookEntryKey();
	}

	/**
	 * Returns the tokens values of this style book entry.
	 *
	 * @return the tokens values of this style book entry
	 */
	@Override
	public String getTokensValues() {
		return model.getTokensValues();
	}

	/**
	 * Returns the user ID of this style book entry.
	 *
	 * @return the user ID of this style book entry
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user name of this style book entry.
	 *
	 * @return the user name of this style book entry
	 */
	@Override
	public String getUserName() {
		return model.getUserName();
	}

	/**
	 * Returns the user uuid of this style book entry.
	 *
	 * @return the user uuid of this style book entry
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	/**
	 * Returns <code>true</code> if this style book entry is default style book entry.
	 *
	 * @return <code>true</code> if this style book entry is default style book entry; <code>false</code> otherwise
	 */
	@Override
	public boolean isDefaultStyleBookEntry() {
		return model.isDefaultStyleBookEntry();
	}

	@Override
	public void persist() {
		model.persist();
	}

	@Override
	public void populateZipWriter(
			com.liferay.portal.kernel.zip.ZipWriter zipWriter, String path)
		throws Exception {

		model.populateZipWriter(zipWriter, path);
	}

	/**
	 * Sets the company ID of this style book entry.
	 *
	 * @param companyId the company ID of this style book entry
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this style book entry.
	 *
	 * @param createDate the create date of this style book entry
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets whether this style book entry is default style book entry.
	 *
	 * @param defaultStyleBookEntry the default style book entry of this style book entry
	 */
	@Override
	public void setDefaultStyleBookEntry(boolean defaultStyleBookEntry) {
		model.setDefaultStyleBookEntry(defaultStyleBookEntry);
	}

	/**
	 * Sets the group ID of this style book entry.
	 *
	 * @param groupId the group ID of this style book entry
	 */
	@Override
	public void setGroupId(long groupId) {
		model.setGroupId(groupId);
	}

	/**
	 * Sets the head ID of this style book entry.
	 *
	 * @param headId the head ID of this style book entry
	 */
	@Override
	public void setHeadId(long headId) {
		model.setHeadId(headId);
	}

	/**
	 * Sets the mvcc version of this style book entry.
	 *
	 * @param mvccVersion the mvcc version of this style book entry
	 */
	@Override
	public void setMvccVersion(long mvccVersion) {
		model.setMvccVersion(mvccVersion);
	}

	/**
	 * Sets the name of this style book entry.
	 *
	 * @param name the name of this style book entry
	 */
	@Override
	public void setName(String name) {
		model.setName(name);
	}

	/**
	 * Sets the preview file entry ID of this style book entry.
	 *
	 * @param previewFileEntryId the preview file entry ID of this style book entry
	 */
	@Override
	public void setPreviewFileEntryId(long previewFileEntryId) {
		model.setPreviewFileEntryId(previewFileEntryId);
	}

	/**
	 * Sets the primary key of this style book entry.
	 *
	 * @param primaryKey the primary key of this style book entry
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the style book entry ID of this style book entry.
	 *
	 * @param styleBookEntryId the style book entry ID of this style book entry
	 */
	@Override
	public void setStyleBookEntryId(long styleBookEntryId) {
		model.setStyleBookEntryId(styleBookEntryId);
	}

	/**
	 * Sets the style book entry key of this style book entry.
	 *
	 * @param styleBookEntryKey the style book entry key of this style book entry
	 */
	@Override
	public void setStyleBookEntryKey(String styleBookEntryKey) {
		model.setStyleBookEntryKey(styleBookEntryKey);
	}

	/**
	 * Sets the tokens values of this style book entry.
	 *
	 * @param tokensValues the tokens values of this style book entry
	 */
	@Override
	public void setTokensValues(String tokensValues) {
		model.setTokensValues(tokensValues);
	}

	/**
	 * Sets the user ID of this style book entry.
	 *
	 * @param userId the user ID of this style book entry
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user name of this style book entry.
	 *
	 * @param userName the user name of this style book entry
	 */
	@Override
	public void setUserName(String userName) {
		model.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this style book entry.
	 *
	 * @param userUuid the user uuid of this style book entry
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	@Override
	public boolean isHead() {
		return model.isHead();
	}

	@Override
	public void populateVersionModel(
		StyleBookEntryVersion styleBookEntryVersion) {

		model.populateVersionModel(styleBookEntryVersion);
	}

	@Override
	protected StyleBookEntryWrapper wrap(StyleBookEntry styleBookEntry) {
		return new StyleBookEntryWrapper(styleBookEntry);
	}

}
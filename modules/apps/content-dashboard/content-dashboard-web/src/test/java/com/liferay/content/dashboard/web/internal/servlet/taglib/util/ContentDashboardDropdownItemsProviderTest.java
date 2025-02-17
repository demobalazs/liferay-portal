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

package com.liferay.content.dashboard.web.internal.servlet.taglib.util;

import com.liferay.content.dashboard.web.internal.item.ContentDashboardItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletRenderRequest;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletURL;
import com.liferay.portal.kernel.test.util.PropsTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.language.LanguageImpl;
import com.liferay.portal.language.LanguageResources;
import com.liferay.portal.util.HtmlImpl;
import com.liferay.portal.util.HttpImpl;
import com.liferay.portal.util.PortalImpl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Cristina González
 */
public class ContentDashboardDropdownItemsProviderTest {

	@BeforeClass
	public static void setUpClass() {
		HtmlUtil htmlUtil = new HtmlUtil();

		htmlUtil.setHtml(new HtmlImpl());

		_http = new HttpImpl();

		_language = new LanguageImpl();

		LanguageResources languageResources = new LanguageResources();

		languageResources.setConfig(StringPool.BLANK);

		LanguageUtil languageUtil = new LanguageUtil();

		languageUtil.setLanguage(_language);

		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(new PortalImpl());

		PropsTestUtil.setProps(Collections.emptyMap());
	}

	@Test
	public void testGetEditURL() {
		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			new MockLiferayPortletRenderRequest();

		MockLiferayPortletURL mockLiferayPortletURL =
			new MockLiferayPortletURL();

		mockLiferayPortletRenderRequest.setAttribute(
			"null" + StringPool.DASH + WebKeys.CURRENT_PORTLET_URL,
			mockLiferayPortletURL);

		mockLiferayPortletRenderRequest.setAttribute(
			WebKeys.LOCALE, LocaleUtil.US);

		ContentDashboardDropdownItemsProvider
			contentDashboardDropdownItemsProvider =
				new ContentDashboardDropdownItemsProvider(
					_http, _language, mockLiferayPortletRenderRequest, null,
					new PortalImpl());

		ContentDashboardItem contentDashboardItem = Mockito.mock(
			ContentDashboardItem.class);

		Mockito.when(
			contentDashboardItem.isEditURLEnabled(
				Mockito.any(HttpServletRequest.class))
		).thenReturn(
			true
		);

		Mockito.when(
			contentDashboardItem.getEditURL(
				Mockito.any(HttpServletRequest.class))
		).thenReturn(
			"validURL"
		);

		List<DropdownItem> dropdownItems =
			contentDashboardDropdownItemsProvider.getDropdownItems(
				contentDashboardItem);

		Stream<DropdownItem> stream = dropdownItems.stream();

		DropdownItem editDropdownItem = stream.filter(
			dropdownItem -> Objects.equals(
				String.valueOf(dropdownItem.get("label")), "edit")
		).findFirst(
		).orElseThrow(
			() -> new AssertionError()
		);

		Assert.assertEquals(
			"validURL",
			_http.getPath(String.valueOf(editDropdownItem.get("href"))));
	}

	@Test
	public void testGetURLBackURL() {
		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			new MockLiferayPortletRenderRequest();

		MockLiferayPortletURL mockLiferayPortletURL =
			new MockLiferayPortletURL();

		mockLiferayPortletRenderRequest.setAttribute(
			"null" + StringPool.DASH + WebKeys.CURRENT_PORTLET_URL,
			mockLiferayPortletURL);

		ContentDashboardDropdownItemsProvider
			contentDashboardDropdownItemsProvider =
				new ContentDashboardDropdownItemsProvider(
					_http, _language, mockLiferayPortletRenderRequest, null,
					new PortalImpl());

		ContentDashboardItem contentDashboardItem = Mockito.mock(
			ContentDashboardItem.class);

		Mockito.when(
			contentDashboardItem.isViewURLEnabled(
				Mockito.any(HttpServletRequest.class))
		).thenReturn(
			true
		);

		Mockito.when(
			contentDashboardItem.getViewURL(
				Mockito.any(HttpServletRequest.class))
		).thenReturn(
			"validURL"
		);

		List<DropdownItem> dropdownItems =
			contentDashboardDropdownItemsProvider.getDropdownItems(
				contentDashboardItem);

		Stream<DropdownItem> stream = dropdownItems.stream();

		DropdownItem viewDropdownItem = stream.filter(
			dropdownItem -> Objects.equals(
				String.valueOf(dropdownItem.get("label")), "view")
		).findFirst(
		).orElseThrow(
			() -> new AssertionError()
		);

		Assert.assertEquals(
			HtmlUtil.escapeURL(String.valueOf(mockLiferayPortletURL)),
			_http.getParameter(
				String.valueOf(viewDropdownItem.get("href")), "p_l_back_url"));
	}

	@Test
	public void testGetURLBackURLWithBackURLParameter() {
		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			new MockLiferayPortletRenderRequest();

		mockLiferayPortletRenderRequest.setAttribute(
			"null" + StringPool.DASH + WebKeys.CURRENT_PORTLET_URL,
			new MockLiferayPortletURL());

		String backURL = RandomTestUtil.randomString();

		mockLiferayPortletRenderRequest.setParameter("backURL", backURL);

		ContentDashboardDropdownItemsProvider
			contentDashboardDropdownItemsProvider =
				new ContentDashboardDropdownItemsProvider(
					_http, _language, mockLiferayPortletRenderRequest, null,
					new PortalImpl());

		ContentDashboardItem contentDashboardItem = Mockito.mock(
			ContentDashboardItem.class);

		Mockito.when(
			contentDashboardItem.isViewURLEnabled(
				Mockito.any(HttpServletRequest.class))
		).thenReturn(
			true
		);

		Mockito.when(
			contentDashboardItem.getViewURL(
				Mockito.any(HttpServletRequest.class))
		).thenReturn(
			"validURL"
		);

		List<DropdownItem> dropdownItems =
			contentDashboardDropdownItemsProvider.getDropdownItems(
				contentDashboardItem);

		Stream<DropdownItem> stream = dropdownItems.stream();

		DropdownItem viewDropdownItem = stream.filter(
			dropdownItem -> Objects.equals(
				String.valueOf(dropdownItem.get("label")), "view")
		).findFirst(
		).orElseThrow(
			() -> new AssertionError()
		);

		Assert.assertEquals(
			backURL,
			_http.getParameter(
				String.valueOf(viewDropdownItem.get("href")), "p_l_back_url"));
	}

	@Test
	public void testGetViewURL() {
		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			new MockLiferayPortletRenderRequest();

		MockLiferayPortletURL mockLiferayPortletURL =
			new MockLiferayPortletURL();

		mockLiferayPortletRenderRequest.setAttribute(
			"null" + StringPool.DASH + WebKeys.CURRENT_PORTLET_URL,
			mockLiferayPortletURL);

		mockLiferayPortletRenderRequest.setAttribute(
			WebKeys.LOCALE, LocaleUtil.US);

		ContentDashboardDropdownItemsProvider
			contentDashboardDropdownItemsProvider =
				new ContentDashboardDropdownItemsProvider(
					_http, _language, mockLiferayPortletRenderRequest, null,
					new PortalImpl());

		ContentDashboardItem contentDashboardItem = Mockito.mock(
			ContentDashboardItem.class);

		Mockito.when(
			contentDashboardItem.isViewURLEnabled(
				Mockito.any(HttpServletRequest.class))
		).thenReturn(
			true
		);

		Mockito.when(
			contentDashboardItem.getViewURL(
				Mockito.any(HttpServletRequest.class))
		).thenReturn(
			"validURL"
		);

		List<DropdownItem> dropdownItems =
			contentDashboardDropdownItemsProvider.getDropdownItems(
				contentDashboardItem);

		Stream<DropdownItem> stream = dropdownItems.stream();

		DropdownItem viewDropdownItem = stream.filter(
			dropdownItem -> Objects.equals(
				String.valueOf(dropdownItem.get("label")), "view")
		).findFirst(
		).orElseThrow(
			() -> new AssertionError()
		);

		Assert.assertEquals(
			"validURL",
			_http.getPath(String.valueOf(viewDropdownItem.get("href"))));
	}

	private static Http _http;
	private static Language _language;

}
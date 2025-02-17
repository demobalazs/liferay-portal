/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.analytics.reports.web.internal.display.context;

import com.liferay.analytics.reports.info.item.AnalyticsReportsInfoItem;
import com.liferay.analytics.reports.web.internal.configuration.AnalyticsReportsConfiguration;
import com.liferay.analytics.reports.web.internal.data.provider.AnalyticsReportsDataProvider;
import com.liferay.analytics.reports.web.internal.model.CountrySearchKeywords;
import com.liferay.analytics.reports.web.internal.model.SearchKeyword;
import com.liferay.analytics.reports.web.internal.model.TrafficSource;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.FastDateFormatFactoryImpl;
import com.liferay.portal.util.PortalImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author David Arques
 */
@RunWith(MockitoJUnitRunner.class)
public class AnalyticsReportsDisplayContextTest {

	@BeforeClass
	public static void setUpClass() {
		FastDateFormatFactoryUtil fastDateFormatFactoryUtil =
			new FastDateFormatFactoryUtil();

		fastDateFormatFactoryUtil.setFastDateFormatFactory(
			new FastDateFormatFactoryImpl());

		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(new JSONFactoryImpl());
	}

	@Test
	public void testGetContextWithInvalidAnalyticsConnection() {
		LocalDate localDate = LocalDate.now();

		AnalyticsReportsDisplayContext analyticsReportsDisplayContext =
			new AnalyticsReportsDisplayContext(
				_getAnalyticsReportsConfiguration(true, false),
				_getAnalyticsReportsDataProvider(
					null, RandomTestUtil.randomInt(),
					RandomTestUtil.randomDouble(), null,
					RandomTestUtil.randomInt(), RandomTestUtil.randomDouble(),
					false),
				_getAnalyticsReportsItem(), null, null, new PortalImpl(),
				_getRenderResponse(), _getResourceBundle(),
				_getThemeDisplay(_getLayout()));

		Map<String, Object> data = analyticsReportsDisplayContext.getData();

		Map<String, Object> context = (Map<String, Object>)data.get("context");

		Map<String, Object> defaultTimeRange = (Map<String, Object>)context.get(
			"defaultTimeRange");

		Assert.assertEquals(
			DateTimeFormatter.ISO_DATE.format(
				localDate.minus(1, ChronoUnit.DAYS)),
			defaultTimeRange.get("endDate"));
		Assert.assertEquals(
			DateTimeFormatter.ISO_DATE.format(
				localDate.minus(7, ChronoUnit.DAYS)),
			defaultTimeRange.get("startDate"));

		Assert.assertTrue((Boolean)context.get("readsEnabled"));
	}

	@Test
	public void testGetContextWithReadsDisabled() {
		AnalyticsReportsDisplayContext analyticsReportsDisplayContext =
			new AnalyticsReportsDisplayContext(
				_getAnalyticsReportsConfiguration(false, false),
				_getAnalyticsReportsDataProvider(
					null, RandomTestUtil.randomInt(),
					RandomTestUtil.randomDouble(), null,
					RandomTestUtil.randomInt(), RandomTestUtil.randomDouble(),
					false),
				_getAnalyticsReportsItem(), null, null, new PortalImpl(),
				_getRenderResponse(), _getResourceBundle(),
				_getThemeDisplay(_getLayout()));

		Map<String, Object> data = analyticsReportsDisplayContext.getData();

		Map<String, Object> context = (Map<String, Object>)data.get("context");

		Assert.assertFalse((Boolean)context.get("readsEnabled"));
	}

	@Test
	public void testGetPropsWithInvalidAnalyticsConnection() {
		long organicTrafficAmount = RandomTestUtil.randomInt();
		double organicTrafficShare = RandomTestUtil.randomDouble();

		long paidTrafficAmount = RandomTestUtil.randomInt();
		double paidTrafficShare = RandomTestUtil.randomDouble();

		AnalyticsReportsDataProvider analyticsReportsDataProvider =
			_getAnalyticsReportsDataProvider(
				null, organicTrafficAmount, organicTrafficShare, null,
				paidTrafficAmount, paidTrafficShare, false);

		AnalyticsReportsInfoItem<Object> analyticsReportsInfoItem =
			_getAnalyticsReportsItem();

		Layout layout = _getLayout();

		AnalyticsReportsDisplayContext analyticsReportsDisplayContext =
			new AnalyticsReportsDisplayContext(
				_getAnalyticsReportsConfiguration(false, true),
				analyticsReportsDataProvider, analyticsReportsInfoItem, null,
				null, new PortalImpl(), _getRenderResponse(),
				_getResourceBundle(), _getThemeDisplay(layout));

		Map<String, Object> data = analyticsReportsDisplayContext.getData();

		Map<String, Object> props = (Map<String, Object>)data.get("props");

		Assert.assertEquals(
			analyticsReportsInfoItem.getAuthorName(null),
			props.get("authorName"));
		Assert.assertEquals(
			analyticsReportsInfoItem.getPublishDate(null),
			props.get("publishDate"));
		Assert.assertEquals(
			analyticsReportsInfoItem.getTitle(null, LocaleUtil.US),
			props.get("title"));

		JSONArray trafficSourcesJSONArray = (JSONArray)props.get(
			"trafficSources");

		Assert.assertEquals(
			JSONUtil.putAll(
				JSONUtil.put(
					"helpMessage", _titles.get(_MESSAGE_KEY_HELP_PAID)
				).put(
					"name", _TITLE_KEY_PAID
				).put(
					"title", _titles.get(_TITLE_KEY_PAID)
				),
				JSONUtil.put(
					"helpMessage", _titles.get(_MESSAGE_KEY_HELP_ORGANIC)
				).put(
					"name", _TITLE_KEY_ORGANIC
				).put(
					"title", _titles.get(_TITLE_KEY_ORGANIC)
				)
			).toJSONString(),
			trafficSourcesJSONArray.toJSONString());
	}

	@Test
	public void testGetPropsWithTrafficSourcesDisabled() {
		long organicTrafficAmount = RandomTestUtil.randomInt();
		double organicTrafficShare = RandomTestUtil.randomDouble();

		long paidTrafficAmount = RandomTestUtil.randomInt();
		double paidTrafficShare = RandomTestUtil.randomDouble();

		AnalyticsReportsDataProvider analyticsReportsDataProvider =
			_getAnalyticsReportsDataProvider(
				null, organicTrafficAmount, organicTrafficShare, null,
				paidTrafficAmount, paidTrafficShare, false);

		AnalyticsReportsInfoItem<Object> analyticsReportsInfoItem =
			_getAnalyticsReportsItem();

		Layout layout = _getLayout();

		AnalyticsReportsDisplayContext analyticsReportsDisplayContext =
			new AnalyticsReportsDisplayContext(
				_getAnalyticsReportsConfiguration(false, false),
				analyticsReportsDataProvider, analyticsReportsInfoItem, null,
				null, new PortalImpl(), _getRenderResponse(),
				_getResourceBundle(), _getThemeDisplay(layout));

		Map<String, Object> data = analyticsReportsDisplayContext.getData();

		Map<String, Object> props = (Map<String, Object>)data.get("props");

		JSONArray trafficSourcesJSONArray = (JSONArray)props.get(
			"trafficSources");

		Assert.assertEquals("[]", trafficSourcesJSONArray.toJSONString());
	}

	@Test
	public void testGetPropsWithValidAnalyticsConnection() {
		long organicTrafficAmount = RandomTestUtil.randomInt();
		double organicTrafficShare = RandomTestUtil.randomDouble();

		long paidTrafficAmount = RandomTestUtil.randomInt();
		double paidTrafficShare = RandomTestUtil.randomDouble();

		SearchKeyword organicSearchKeyword = new SearchKeyword(
			RandomTestUtil.randomString(), RandomTestUtil.randomInt(),
			RandomTestUtil.randomInt(), RandomTestUtil.randomInt());

		SearchKeyword paidSearchKeyword = new SearchKeyword(
			RandomTestUtil.randomString(), RandomTestUtil.randomInt(),
			RandomTestUtil.randomInt(), RandomTestUtil.randomInt());

		AnalyticsReportsDataProvider analyticsReportsDataProvider =
			_getAnalyticsReportsDataProvider(
				Collections.singletonList(organicSearchKeyword),
				organicTrafficAmount, organicTrafficShare,
				Collections.singletonList(paidSearchKeyword), paidTrafficAmount,
				paidTrafficShare, true);

		AnalyticsReportsInfoItem<Object> analyticsReportsInfoItem =
			_getAnalyticsReportsItem();

		Layout layout = _getLayout();

		AnalyticsReportsDisplayContext analyticsReportsDisplayContext =
			new AnalyticsReportsDisplayContext(
				_getAnalyticsReportsConfiguration(false, true),
				analyticsReportsDataProvider, analyticsReportsInfoItem, null,
				null, new PortalImpl(), _getRenderResponse(),
				_getResourceBundle(), _getThemeDisplay(layout));

		Map<String, Object> data = analyticsReportsDisplayContext.getData();

		Map<String, Object> props = (Map<String, Object>)data.get("props");

		Assert.assertEquals(
			analyticsReportsInfoItem.getAuthorName(null),
			props.get("authorName"));

		Assert.assertEquals(
			analyticsReportsInfoItem.getPublishDate(null),
			props.get("publishDate"));

		Assert.assertEquals(
			analyticsReportsInfoItem.getTitle(null, LocaleUtil.US),
			props.get("title"));

		JSONArray trafficSourcesJSONArray = (JSONArray)props.get(
			"trafficSources");

		Assert.assertEquals(
			JSONUtil.putAll(
				JSONUtil.put(
					"countryKeywords",
					JSONUtil.putAll(
						JSONUtil.put(
							"countryCode", "us"
						).put(
							"countryName", "United States"
						).put(
							"keywords",
							JSONUtil.putAll(
								JSONUtil.put(
									"keyword", paidSearchKeyword.getKeyword()
								).put(
									"position", paidSearchKeyword.getPosition()
								).put(
									"searchVolume",
									paidSearchKeyword.getSearchVolume()
								).put(
									"traffic",
									Math.toIntExact(
										paidSearchKeyword.getTraffic())
								))
						))
				).put(
					"helpMessage", _titles.get(_MESSAGE_KEY_HELP_PAID)
				).put(
					"name", _TITLE_KEY_PAID
				).put(
					"share", paidTrafficShare
				).put(
					"title", _titles.get(_TITLE_KEY_PAID)
				).put(
					"value", Math.toIntExact(paidTrafficAmount)
				),
				JSONUtil.put(
					"countryKeywords",
					JSONUtil.putAll(
						JSONUtil.put(
							"countryCode", "us"
						).put(
							"countryName", "United States"
						).put(
							"keywords",
							JSONUtil.putAll(
								JSONUtil.put(
									"keyword", organicSearchKeyword.getKeyword()
								).put(
									"position",
									organicSearchKeyword.getPosition()
								).put(
									"searchVolume",
									organicSearchKeyword.getSearchVolume()
								).put(
									"traffic",
									Math.toIntExact(
										organicSearchKeyword.getTraffic())
								))
						))
				).put(
					"helpMessage", _titles.get(_MESSAGE_KEY_HELP_ORGANIC)
				).put(
					"name", _TITLE_KEY_ORGANIC
				).put(
					"share", organicTrafficShare
				).put(
					"title", _titles.get(_TITLE_KEY_ORGANIC)
				).put(
					"value", Math.toIntExact(organicTrafficAmount)
				)
			).toJSONString(),
			trafficSourcesJSONArray.toJSONString());
	}

	private AnalyticsReportsConfiguration _getAnalyticsReportsConfiguration(
		boolean readsEnabled, boolean trafficSourcesEnabled) {

		return new AnalyticsReportsConfiguration() {

			@Override
			public boolean readsEnabled() {
				return readsEnabled;
			}

			@Override
			public boolean trafficSourcesEnabled() {
				return trafficSourcesEnabled;
			}

		};
	}

	private AnalyticsReportsDataProvider _getAnalyticsReportsDataProvider(
		List<SearchKeyword> organicSearchKeywords, long organicTrafficAmount,
		double organicTrafficShare, List<SearchKeyword> paidSearchKeywords,
		long paidTrafficAmount, double paidTrafficShare,
		boolean validAnalyticsConnection) {

		return new AnalyticsReportsDataProvider(Mockito.mock(Http.class)) {

			@Override
			public List<TrafficSource> getTrafficSources(
				long companyId, String url) {

				return Arrays.asList(
					new TrafficSource(
						Collections.singletonList(
							new CountrySearchKeywords(
								"us", organicSearchKeywords)),
						_TITLE_KEY_ORGANIC, organicTrafficAmount,
						organicTrafficShare),
					new TrafficSource(
						Collections.singletonList(
							new CountrySearchKeywords(
								"us", paidSearchKeywords)),
						_TITLE_KEY_PAID, paidTrafficAmount, paidTrafficShare));
			}

			@Override
			public boolean isValidAnalyticsConnection(long companyId) {
				return validAnalyticsConnection;
			}

		};
	}

	private AnalyticsReportsInfoItem<Object> _getAnalyticsReportsItem() {
		String authorName = StringUtil.randomString();
		Date publishDate = RandomTestUtil.nextDate();
		String title = StringUtil.randomString();

		return new AnalyticsReportsInfoItem() {

			@Override
			public String getAuthorName(Object model) {
				return authorName;
			}

			@Override
			public Date getPublishDate(Object model) {
				return publishDate;
			}

			@Override
			public String getTitle(Object model, Locale locale) {
				return title;
			}

		};
	}

	private Layout _getLayout() {
		Layout layout = Mockito.mock(Layout.class);

		Date date = RandomTestUtil.nextDate();

		Mockito.doReturn(
			date
		).when(
			layout
		).getPublishDate();

		return layout;
	}

	private RenderResponse _getRenderResponse() {
		RenderResponse renderResponse = Mockito.mock(RenderResponse.class);

		Mockito.when(
			renderResponse.createResourceURL()
		).thenReturn(
			Mockito.mock(ResourceURL.class)
		);

		return renderResponse;
	}

	private ResourceBundle _getResourceBundle() {
		return new ResourceBundle() {

			@Override
			public Enumeration<String> getKeys() {
				return Collections.enumeration(
					Arrays.asList(
						_TITLE_KEY_ORGANIC, _TITLE_KEY_PAID,
						_MESSAGE_KEY_HELP_ORGANIC, _MESSAGE_KEY_HELP_PAID));
			}

			@Override
			protected Object handleGetObject(String key) {
				return _titles.getOrDefault(key, key);
			}

		};
	}

	private ThemeDisplay _getThemeDisplay(Layout layout) {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		Company company = Mockito.mock(Company.class);

		try {
			themeDisplay.setCompany(company);
		}
		catch (PortalException portalException) {
			throw new AssertionError("Error found ", portalException);
		}

		themeDisplay.setLayout(layout);

		themeDisplay.setLocale(LocaleUtil.US);

		return themeDisplay;
	}

	private static final String _MESSAGE_KEY_HELP_ORGANIC =
		"this-number-refers-to-the-volume-of-people-that-find-your-page-" +
			"through-a-search-engine";

	private static final String _MESSAGE_KEY_HELP_PAID =
		"this-number-refers-to-the-volume-of-people-that-find-your-page-" +
			"through-paid-keywords";

	private static final String _TITLE_KEY_ORGANIC = "organic";

	private static final String _TITLE_KEY_PAID = "paid";

	private final Map<String, String> _titles = HashMapBuilder.put(
		_MESSAGE_KEY_HELP_ORGANIC, "helpMessageOrganic"
	).put(
		_MESSAGE_KEY_HELP_PAID, "helpMessagePaid"
	).put(
		_TITLE_KEY_ORGANIC, "organic"
	).put(
		_TITLE_KEY_PAID, "paid"
	).build();

}
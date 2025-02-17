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

package com.liferay.layout.page.template.admin.web.internal.portlet.action.test;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.fragment.constants.FragmentConstants;
import com.liferay.fragment.model.FragmentCollection;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.service.FragmentCollectionLocalService;
import com.liferay.fragment.service.FragmentEntryLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.layout.page.template.constants.LayoutPageTemplateExportImportConstants;
import com.liferay.layout.page.template.importer.LayoutPageTemplatesImporter;
import com.liferay.layout.page.template.importer.LayoutPageTemplatesImporterResultEntry;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletActionResponse;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.kernel.zip.ZipWriterFactoryUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Rubén Pulido
 */
@RunWith(Arquillian.class)
public class ImportExportLayoutPageTemplateEntriesTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_bundle = FrameworkUtil.getBundle(getClass());

		_group = GroupTestUtil.addGroup();

		_company = _companyLocalService.getCompany(_group.getCompanyId());

		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(new JSONFactoryImpl());

		_objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
			}
		};
	}

	@Test
	public void testImportExportLayoutPageTemplateEntryContainerBackgroundFragmentImage()
		throws Exception {

		Map<String, String> valuesMap = HashMapBuilder.put(
			"CLASS_PK",
			() -> {
				JournalArticle journalArticle = _addJournalArticle(
					_group.getGroupId());

				return String.valueOf(journalArticle.getResourcePrimKey());
			}
		).build();

		File expectedFile = _generateZipFile(
			"container/background_fragment_image/expected", valuesMap);
		File inputFile = _generateZipFile(
			"container/background_fragment_image/input", valuesMap);

		_validateImportExport(expectedFile, inputFile);
	}

	@Test
	public void testImportExportLayoutPageTemplateEntryContainerBackgroundImage()
		throws Exception {

		Map<String, String> valuesMap = HashMapBuilder.put(
			"CLASS_PK",
			() -> {
				JournalArticle journalArticle = _addJournalArticle(
					_group.getGroupId());

				return String.valueOf(journalArticle.getResourcePrimKey());
			}
		).build();

		File expectedFile = _generateZipFile(
			"container/background_image/expected", valuesMap);

		File inputFile = _generateZipFile(
			"container/background_image/input", valuesMap);

		_validateImportExport(expectedFile, inputFile);
	}

	@Test
	public void testImportExportLayoutPageTemplateEntryContainerDefault()
		throws Exception {

		File expectedFile = _generateZipFile(
			"container/default/expected", null);

		File inputFile = _generateZipFile("container/default/input", null);

		_validateImportExport(expectedFile, inputFile);
	}

	@Test
	public void testImportExportLayoutPageTemplateEntryContainerEmpty()
		throws Exception {

		File expectedFile = _generateZipFile("container/empty/expected", null);

		File inputFile = _generateZipFile("container/empty/input", null);

		_validateImportExport(expectedFile, inputFile);
	}

	@Test
	public void testImportExportLayoutPageTemplateEntryContainerLayout()
		throws Exception {

		File expectedFile = _generateZipFile("container/layout/expected", null);

		File inputFile = _generateZipFile("container/layout/input", null);

		_validateImportExport(expectedFile, inputFile);
	}

	@Test
	public void testImportExportLayoutPageTemplateEntryFragmentTextFieldFragmentAvailableMappedContentAvailable()
		throws Exception {

		_addTextFragmentEntry();

		Map<String, String> valuesMap = HashMapBuilder.put(
			"CLASS_PK",
			() -> {
				JournalArticle journalArticle = _addJournalArticle(
					_group.getGroupId());

				return String.valueOf(journalArticle.getResourcePrimKey());
			}
		).build();

		File expectedFile = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/expected" +
				"/fragment_available",
			valuesMap);

		File inputFile = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/input",
			valuesMap);

		_validateImportExport(expectedFile, inputFile);
	}

	@Test
	public void testImportExportLayoutPageTemplateEntryFragmentTextFieldFragmentAvailableMappedContentAvailableOverwriteFalsePageTemplateEntryDoesNotExist()
		throws Exception {

		_addTextFragmentEntry();

		Map<String, String> valuesMap = HashMapBuilder.put(
			"CLASS_PK",
			() -> {
				JournalArticle journalArticle = _addJournalArticle(
					_group.getGroupId());

				return String.valueOf(journalArticle.getResourcePrimKey());
			}
		).build();

		File expectedFile = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/expected" +
				"/fragment_available",
			valuesMap);

		File inputFile = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/input",
			valuesMap);

		_validateImportExport(expectedFile, inputFile);
	}

	@Test
	public void testImportExportLayoutPageTemplateEntryFragmentTextFieldFragmentAvailableMappedContentAvailableOverwriteFalsePageTemplateEntryExists()
		throws Exception {

		_addTextFragmentEntry();

		Map<String, String> valuesMap1 = HashMapBuilder.put(
			"CLASS_PK",
			() -> {
				JournalArticle journalArticle = _addJournalArticle(
					_group.getGroupId());

				return String.valueOf(journalArticle.getResourcePrimKey());
			}
		).build();

		File expectedFile = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/expected" +
				"/fragment_available",
			valuesMap1);

		File inputFile1 = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/input",
			valuesMap1);

		_getImportLayoutPageTemplateEntry(
			inputFile1, _group.getGroupId(), false,
			LayoutPageTemplatesImporterResultEntry.Status.IMPORTED);

		Map<String, String> valuesMap2 = HashMapBuilder.put(
			"CLASS_PK",
			() -> {
				JournalArticle journalArticle = _addJournalArticle(
					_group.getGroupId());

				return String.valueOf(journalArticle.getResourcePrimKey());
			}
		).build();

		File inputFile2 = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/expected" +
				"/fragment_available",
			valuesMap2);

		File outputFile = _importExportLayoutPageTemplateEntry(
			inputFile2, _group.getGroupId(), false,
			LayoutPageTemplatesImporterResultEntry.Status.IGNORED);

		_validateFile(expectedFile, outputFile);
	}

	@Test
	public void testImportExportLayoutPageTemplateEntryFragmentTextFieldFragmentAvailableMappedContentAvailableOverwriteTruePageTemplateEntryDoesNotExist()
		throws Exception {

		_addTextFragmentEntry();

		Map<String, String> valuesMap = HashMapBuilder.put(
			"CLASS_PK",
			() -> {
				JournalArticle journalArticle = _addJournalArticle(
					_group.getGroupId());

				return String.valueOf(journalArticle.getResourcePrimKey());
			}
		).build();

		File expectedFile = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/expected" +
				"/fragment_available",
			valuesMap);

		File inputFile = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/input",
			valuesMap);

		File outputFile = _importExportLayoutPageTemplateEntry(
			inputFile, _group.getGroupId(), true,
			LayoutPageTemplatesImporterResultEntry.Status.IMPORTED);

		_validateFile(expectedFile, outputFile);
	}

	@Test
	public void testImportExportLayoutPageTemplateEntryFragmentTextFieldFragmentAvailableMappedContentAvailableOverwriteTruePageTemplateEntryExists()
		throws Exception {

		_addTextFragmentEntry();

		Map<String, String> valuesMap1 = HashMapBuilder.put(
			"CLASS_PK",
			() -> {
				JournalArticle journalArticle = _addJournalArticle(
					_group.getGroupId());

				return String.valueOf(journalArticle.getResourcePrimKey());
			}
		).build();

		File inputFile1 = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/input",
			valuesMap1);

		_getImportLayoutPageTemplateEntry(
			inputFile1, _group.getGroupId(), false,
			LayoutPageTemplatesImporterResultEntry.Status.IMPORTED);

		Map<String, String> valuesMap2 = HashMapBuilder.put(
			"CLASS_PK",
			() -> {
				JournalArticle journalArticle = _addJournalArticle(
					_group.getGroupId());

				return String.valueOf(journalArticle.getResourcePrimKey());
			}
		).build();

		File expectedFile = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/expected" +
				"/fragment_available",
			valuesMap2);

		File inputFile2 = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/input",
			valuesMap2);

		File outputFile = _importExportLayoutPageTemplateEntry(
			inputFile2, _group.getGroupId(), true,
			LayoutPageTemplatesImporterResultEntry.Status.IMPORTED);

		_validateFile(expectedFile, outputFile);
	}

	@Test
	public void testImportExportLayoutPageTemplateEntryFragmentTextFieldFragmentAvailableMappedContentNotAvailable()
		throws Exception {

		_addTextFragmentEntry();

		Map<String, String> valuesMap = HashMapBuilder.put(
			"CLASS_PK", String.valueOf(RandomTestUtil.randomLong())
		).build();

		File expectedFile = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/expected" +
				"/fragment_available",
			valuesMap);

		File inputFile = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/input",
			valuesMap);

		_validateImportExport(expectedFile, inputFile);
	}

	@Test
	public void testImportExportLayoutPageTemplateEntryFragmentTextFieldFragmentNotAvailable()
		throws Exception {

		Map<String, String> valuesMap = HashMapBuilder.put(
			"CLASS_PK",
			() -> {
				JournalArticle journalArticle = _addJournalArticle(
					_group.getGroupId());

				return String.valueOf(journalArticle.getResourcePrimKey());
			}
		).build();

		File expectedFile = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/expected" +
				"/fragment_not_available",
			valuesMap);

		File inputFile = _generateZipFile(
			"fragment/text_field/mapped_value/class_pk_reference/input",
			valuesMap);

		_validateImportExport(expectedFile, inputFile);
	}

	@Test
	public void testImportExportLayoutPageTemplateEntryRowContainer()
		throws Exception {

		File expectedFile = _generateZipFile("row/container/expected", null);

		File inputFile = _generateZipFile("row/container/input", null);

		_validateImportExport(expectedFile, inputFile);
	}

	private FragmentEntry _addFragmentEntry(
			long groupId, String key, String name, String html)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(groupId);

		FragmentCollection fragmentCollection =
			_fragmentCollectionLocalService.addFragmentCollection(
				TestPropsValues.getUserId(), groupId, "Test Collection",
				StringPool.BLANK, serviceContext);

		return _fragmentEntryLocalService.addFragmentEntry(
			TestPropsValues.getUserId(), groupId,
			fragmentCollection.getFragmentCollectionId(), key, name,
			StringPool.BLANK, html, StringPool.BLANK, StringPool.BLANK, 0,
			FragmentConstants.TYPE_COMPONENT, WorkflowConstants.STATUS_APPROVED,
			serviceContext);
	}

	private JournalArticle _addJournalArticle(long groupId) throws Exception {
		Map<Locale, String> titleMap = HashMapBuilder.put(
			LocaleUtil.getDefault(), RandomTestUtil.randomString()
		).build();
		Map<Locale, String> contentMap = HashMapBuilder.put(
			LocaleUtil.getDefault(), RandomTestUtil.randomString()
		).build();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				groupId, TestPropsValues.getUserId());

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			groupId, 0,
			_portal.getClassNameId("com.liferay.journal.model.JournalArticle"),
			titleMap, null, contentMap, LocaleUtil.getSiteDefault(), false,
			true, serviceContext);

		journalArticle.setSmallImage(true);
		journalArticle.setSmallImageURL(
			"https://avatars1.githubusercontent.com/u/131436");

		return JournalTestUtil.updateArticle(journalArticle);
	}

	private void _addTextFragmentEntry() throws Exception {
		String html =
			"<lfr-editable id=\"element-text\" type=\"text\">Test Text " +
				"Fragment</lfr-editable>";

		_addFragmentEntry(
			_group.getGroupId(), "test-text-fragment", "Test Text Fragment",
			html);
	}

	private void _addZipWriterEntry(
			ZipWriter zipWriter, URL url, Map<String, String> valuesMap)
		throws IOException {

		String entryPath = url.getPath();

		String zipPath = StringUtil.removeSubstring(
			entryPath, _LAYOUT_PATE_TEMPLATES_PATH);

		String content = StringUtil.read(url.openStream());

		zipWriter.addEntry(
			zipPath, StringUtil.replace(content, "\"${", "}\"", valuesMap));
	}

	private File _generateZipFile(
			String testPath, Map<String, String> valuesMap)
		throws Exception {

		ZipWriter zipWriter = ZipWriterFactoryUtil.getZipWriter();

		StringBuilder sb = new StringBuilder(3);

		sb.append(_LAYOUT_PATE_TEMPLATES_PATH + testPath);
		sb.append(StringPool.FORWARD_SLASH + _ROOT_FOLDER);
		sb.append(StringPool.FORWARD_SLASH);

		Enumeration<URL> enumeration = _bundle.findEntries(
			sb.toString(),
			LayoutPageTemplateExportImportConstants.
				FILE_NAME_PAGE_TEMPLATE_COLLECTION,
			true);

		try {
			while (enumeration.hasMoreElements()) {
				URL url = enumeration.nextElement();

				_populateZipWriter(zipWriter, url, valuesMap);
			}

			return zipWriter.getFile();
		}
		catch (Exception exception) {
			throw new Exception(exception);
		}
	}

	private LayoutPageTemplateEntry _getImportLayoutPageTemplateEntry(
			File file, long groupId, boolean overwrite,
			LayoutPageTemplatesImporterResultEntry.Status status)
		throws Exception {

		List<LayoutPageTemplatesImporterResultEntry>
			layoutPageTemplatesImporterResultEntries = null;

		ServiceContext serviceContext = _getServiceContext(
			_group, TestPropsValues.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			layoutPageTemplatesImporterResultEntries =
				_layoutPageTemplatesImporter.importFile(
					TestPropsValues.getUserId(), groupId, 0, file, overwrite);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		Assert.assertNotNull(layoutPageTemplatesImporterResultEntries);

		Assert.assertEquals(
			layoutPageTemplatesImporterResultEntries.toString(), 1,
			layoutPageTemplatesImporterResultEntries.size());

		LayoutPageTemplatesImporterResultEntry layoutPageTemplateImportEntry =
			layoutPageTemplatesImporterResultEntries.get(0);

		Assert.assertEquals(status, layoutPageTemplateImportEntry.getStatus());

		String layoutPageTemplateEntryKey = StringUtil.toLowerCase(
			layoutPageTemplateImportEntry.getName());

		layoutPageTemplateEntryKey = StringUtil.replace(
			layoutPageTemplateEntryKey, CharPool.SPACE, CharPool.DASH);

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_layoutPageTemplateEntryLocalService.fetchLayoutPageTemplateEntry(
				groupId, layoutPageTemplateEntryKey);

		Assert.assertNotNull(layoutPageTemplateEntry);

		return layoutPageTemplateEntry;
	}

	private ServiceContext _getServiceContext(Group group, long userId)
		throws Exception {

		HttpServletRequest httpServletRequest = new MockHttpServletRequest();

		httpServletRequest.setAttribute(
			JavaConstants.JAVAX_PORTLET_RESPONSE,
			new MockLiferayPortletActionResponse());

		httpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(group, userId);

		serviceContext.setRequest(httpServletRequest);

		return serviceContext;
	}

	private ThemeDisplay _getThemeDisplay() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(_company);
		themeDisplay.setLayout(
			_layoutLocalService.getLayout(TestPropsValues.getPlid()));

		LayoutSet layoutSet = _group.getPublicLayoutSet();

		themeDisplay.setLookAndFeel(layoutSet.getTheme(), null);

		themeDisplay.setPermissionChecker(
			PermissionThreadLocal.getPermissionChecker());
		themeDisplay.setRealUser(TestPropsValues.getUser());
		themeDisplay.setScopeGroupId(_group.getGroupId());
		themeDisplay.setSiteGroupId(_group.getGroupId());
		themeDisplay.setUser(TestPropsValues.getUser());

		return themeDisplay;
	}

	private File _importExportLayoutPageTemplateEntry(
			File file, long groupId, boolean overwrite,
			LayoutPageTemplatesImporterResultEntry.Status status)
		throws Exception {

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getImportLayoutPageTemplateEntry(file, groupId, overwrite, status);

		long[] layoutPageTemplateEntryIds = {
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId()
		};

		return ReflectionTestUtil.invoke(
			_mvcResourceCommand, "getFile", new Class<?>[] {long[].class},
			layoutPageTemplateEntryIds);
	}

	private void _populateZipWriter(
			ZipWriter zipWriter, URL url, Map<String, String> valuesMap)
		throws IOException {

		String zipPath = StringUtil.removeSubstring(
			url.getFile(), _LAYOUT_PATE_TEMPLATES_PATH);

		zipWriter.addEntry(zipPath, url.openStream());

		String path = FileUtil.getPath(url.getPath());

		Enumeration<URL> enumeration = _bundle.findEntries(
			path,
			LayoutPageTemplateExportImportConstants.FILE_NAME_PAGE_TEMPLATE,
			true);

		while (enumeration.hasMoreElements()) {
			URL elementUrl = enumeration.nextElement();

			_addZipWriterEntry(zipWriter, elementUrl, valuesMap);
		}

		enumeration = _bundle.findEntries(
			path,
			LayoutPageTemplateExportImportConstants.FILE_NAME_PAGE_DEFINITION,
			true);

		while (enumeration.hasMoreElements()) {
			URL elementUrl = enumeration.nextElement();

			_addZipWriterEntry(zipWriter, elementUrl, valuesMap);
		}

		enumeration = _bundle.findEntries(path, "thumbnail.png", true);

		if (enumeration == null) {
			return;
		}

		while (enumeration.hasMoreElements()) {
			URL elementUrl = enumeration.nextElement();

			_addZipWriterEntry(zipWriter, elementUrl, valuesMap);
		}
	}

	private void _validateFile(File inputFile, File outputFile)
		throws Exception {

		ZipFile inputZipFile = new ZipFile(inputFile);

		Enumeration<? extends ZipEntry> inputEnumeration =
			inputZipFile.entries();

		Map<String, String> fileNameFileContentMap = new HashMap<>();

		int numberOfInputFiles = 0;

		while (inputEnumeration.hasMoreElements()) {
			ZipEntry zipEntry = inputEnumeration.nextElement();

			if (!zipEntry.isDirectory()) {
				numberOfInputFiles++;

				String content = StringUtil.read(
					inputZipFile.getInputStream(zipEntry));

				String name = zipEntry.getName();

				String[] parts = name.split("/");

				fileNameFileContentMap.put(parts[parts.length - 1], content);
			}
		}

		ZipFile outputZipFile = new ZipFile(outputFile);

		Enumeration<? extends ZipEntry> outputEnumeration =
			outputZipFile.entries();

		int numberOfOutputFiles = 0;

		while (outputEnumeration.hasMoreElements()) {
			ZipEntry zipEntry = outputEnumeration.nextElement();

			if (!zipEntry.isDirectory()) {
				numberOfOutputFiles++;

				String name = zipEntry.getName();

				String[] parts = name.split("/");

				Assert.assertEquals(
					_objectMapper.readTree(
						fileNameFileContentMap.get(parts[parts.length - 1])),
					_objectMapper.readTree(
						StringUtil.read(
							outputZipFile.getInputStream(zipEntry))));
			}
		}

		Assert.assertEquals(numberOfInputFiles, numberOfOutputFiles);
		Assert.assertTrue(numberOfInputFiles > 0);
	}

	private void _validateImportExport(File expectedFile, File inputFile)
		throws Exception {

		File outputFile1 = _importExportLayoutPageTemplateEntry(
			inputFile, _group.getGroupId(), false,
			LayoutPageTemplatesImporterResultEntry.Status.IMPORTED);

		_validateFile(expectedFile, outputFile1);

		File outputFile2 = _importExportLayoutPageTemplateEntry(
			outputFile1, _group.getGroupId(), true,
			LayoutPageTemplatesImporterResultEntry.Status.IMPORTED);

		_validateFile(expectedFile, outputFile2);
	}

	private static final String _LAYOUT_PATE_TEMPLATES_PATH =
		"com/liferay/layout/page/template/admin/web/internal/portlet/action" +
			"/test/dependencies/import_export/page_templates/";

	private static final String _ROOT_FOLDER = "page-templates";

	private Bundle _bundle;
	private Company _company;

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject
	private FragmentCollectionLocalService _fragmentCollectionLocalService;

	@Inject
	private FragmentEntryLocalService _fragmentEntryLocalService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private LayoutLocalService _layoutLocalService;

	@Inject
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Inject
	private LayoutPageTemplatesImporter _layoutPageTemplatesImporter;

	@Inject
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	@Inject(
		filter = "mvc.command.name=/layout_page_template/export_layout_page_template_entry"
	)
	private MVCResourceCommand _mvcResourceCommand;

	private ObjectMapper _objectMapper;

	@Inject
	private Portal _portal;

}
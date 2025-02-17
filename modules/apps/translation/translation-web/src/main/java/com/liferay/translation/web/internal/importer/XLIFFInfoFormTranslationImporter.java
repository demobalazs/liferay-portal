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

package com.liferay.translation.web.internal.importer;

import com.liferay.info.field.InfoField;
import com.liferay.info.field.InfoFieldValue;
import com.liferay.info.field.type.TextInfoFieldType;
import com.liferay.info.item.InfoItemClassPKReference;
import com.liferay.info.item.InfoItemFieldValues;
import com.liferay.info.localized.InfoLocalizedValue;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.translation.exception.XLIFFFileException;
import com.liferay.translation.importer.TranslationInfoItemFieldValuesImporter;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.sf.okapi.lib.xliff2.InvalidParameterException;
import net.sf.okapi.lib.xliff2.XLIFFException;
import net.sf.okapi.lib.xliff2.core.Fragment;
import net.sf.okapi.lib.xliff2.core.Part;
import net.sf.okapi.lib.xliff2.core.StartXliffData;
import net.sf.okapi.lib.xliff2.core.Unit;
import net.sf.okapi.lib.xliff2.document.FileNode;
import net.sf.okapi.lib.xliff2.document.XLIFFDocument;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(
	property = "content.type=application/xliff+xml",
	service = TranslationInfoItemFieldValuesImporter.class
)
public class XLIFFInfoFormTranslationImporter
	implements TranslationInfoItemFieldValuesImporter {

	@Override
	public InfoItemFieldValues importInfoItemFieldValues(
			long groupId, InfoItemClassPKReference infoItemClassPKReference,
			InputStream inputStream)
		throws IOException, XLIFFFileException {

		try {
			XLIFFDocument xliffDocument = new XLIFFDocument();

			xliffDocument.load(FileUtil.createTempFile(inputStream));

			_validateXLIFFFile(
				groupId, infoItemClassPKReference, xliffDocument);

			StartXliffData startXliffData = xliffDocument.getStartXliffData();

			Locale targetLocale = LocaleUtil.fromLanguageId(
				startXliffData.getTargetLanguage(), true, false);

			return InfoItemFieldValues.builder(
			).<XLIFFFileException>infoFieldValue(
				consumer -> {
					for (Unit unit : xliffDocument.getUnits()) {
						if (unit.getPartCount() != 1) {
							throw new XLIFFFileException.MustNotHaveMoreThanOne(
								"The file only can have one unit");
						}

						Part valuePart = unit.getPart(0);

						Fragment value = valuePart.getTarget();

						if (value == null) {
							throw new XLIFFFileException.MustBeWellFormed(
								"There is no translation target");
						}

						InfoField infoField = InfoField.builder(
						).infoFieldType(
							TextInfoFieldType.INSTANCE
						).name(
							unit.getId()
						).labelInfoLocalizedValue(
							InfoLocalizedValue.<String>builder(
							).value(
								targetLocale, unit.getId()
							).build()
						).localizable(
							true
						).build();

						consumer.accept(
							new InfoFieldValue<>(
								infoField,
								InfoLocalizedValue.builder(
								).value(
									targetLocale, value.getPlainText()
								).build()));
					}
				}
			).infoItemClassPKReference(
				infoItemClassPKReference
			).build();
		}
		catch (InvalidParameterException invalidParameterException) {
			throw new XLIFFFileException.MustHaveValidParameter(
				invalidParameterException);
		}
		catch (XLIFFException xliffException) {
			if (xliffException.getCause() instanceof CharConversionException) {
				throw new XLIFFFileException.MustHaveCorrectEncoding(
					xliffException);
			}

			throw new XLIFFFileException.MustBeValid(xliffException);
		}
	}

	private void _validateXLIFFCompletion(
			long groupId, XLIFFDocument xliffDocument)
		throws XLIFFFileException {

		StartXliffData startXliffData = xliffDocument.getStartXliffData();

		String sourceLanguage = startXliffData.getSourceLanguage();

		if (Validator.isNull(sourceLanguage)) {
			throw new XLIFFFileException.MustBeWellFormed(
				"There is no translation source");
		}

		Locale sourceLocale = LocaleUtil.fromLanguageId(
			sourceLanguage, true, false);

		if (sourceLocale == null) {
			throw new XLIFFFileException.MustBeSupportedLanguage(
				sourceLanguage);
		}

		Set<Locale> availableLocales = _language.getAvailableLocales(groupId);

		if (!availableLocales.contains(sourceLocale)) {
			throw new XLIFFFileException.MustBeSupportedLanguage(
				sourceLanguage);
		}

		String targetLanguage = startXliffData.getTargetLanguage();

		if (Validator.isNull(targetLanguage)) {
			throw new XLIFFFileException.MustBeWellFormed(
				"There is no translation target");
		}

		Locale targetLocale = LocaleUtil.fromLanguageId(
			targetLanguage, true, false);

		if (targetLocale == null) {
			throw new XLIFFFileException.MustBeSupportedLanguage(
				targetLanguage);
		}

		if (!availableLocales.contains(targetLocale)) {
			throw new XLIFFFileException.MustBeSupportedLanguage(
				targetLanguage);
		}
	}

	private void _validateXLIFFFile(
			long groupId, InfoItemClassPKReference infoItemClassPKReference,
			XLIFFDocument xliffDocument)
		throws XLIFFFileException {

		_validateXLIFFCompletion(groupId, xliffDocument);

		_validateXLIFFFileNode(infoItemClassPKReference, xliffDocument);
	}

	private void _validateXLIFFFileNode(
			InfoItemClassPKReference infoItemClassPKReference,
			XLIFFDocument xliffDocument)
		throws XLIFFFileException {

		List<String> fileNodeIds = xliffDocument.getFileNodeIds();

		if (fileNodeIds.size() != 1) {
			throw new XLIFFFileException.MustNotHaveMoreThanOne(
				"Only one node is allowed");
		}

		FileNode fileNode = xliffDocument.getFileNode(
			infoItemClassPKReference.getClassName() + StringPool.COLON +
				infoItemClassPKReference.getClassPK());

		if (fileNode == null) {
			throw new XLIFFFileException.MustHaveValidId("File ID is invalid");
		}
	}

	@Reference
	private Language _language;

}
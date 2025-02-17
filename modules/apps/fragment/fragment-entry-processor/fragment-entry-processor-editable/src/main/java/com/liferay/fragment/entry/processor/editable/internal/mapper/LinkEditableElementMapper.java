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

package com.liferay.fragment.entry.processor.editable.internal.mapper;

import com.liferay.fragment.entry.processor.editable.mapper.EditableElementMapper;
import com.liferay.fragment.entry.processor.editable.parser.util.EditableElementParserUtil;
import com.liferay.fragment.entry.processor.helper.FragmentEntryProcessorHelper;
import com.liferay.fragment.processor.FragmentEntryProcessorContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	immediate = true, property = "type=link",
	service = EditableElementMapper.class
)
public class LinkEditableElementMapper implements EditableElementMapper {

	@Override
	public void map(
			Element element, JSONObject configJSONObject,
			FragmentEntryProcessorContext fragmentEntryProcessorContext)
		throws PortalException {

		String href = configJSONObject.getString("href");

		boolean assetDisplayPage =
			_fragmentEntryProcessorHelper.isAssetDisplayPage(
				fragmentEntryProcessorContext.getMode());
		boolean collectionMapped =
			_fragmentEntryProcessorHelper.isMappedCollection(configJSONObject);
		boolean mapped = _fragmentEntryProcessorHelper.isMapped(
			configJSONObject);

		if (Validator.isNull(href) && !assetDisplayPage && !collectionMapped &&
			!mapped) {

			return;
		}

		if (collectionMapped) {
			href = GetterUtil.getString(
				_fragmentEntryProcessorHelper.getMappedCollectionValue(
					configJSONObject, fragmentEntryProcessorContext));
		}
		else if (mapped) {
			href = GetterUtil.getString(
				_fragmentEntryProcessorHelper.getMappedValue(
					configJSONObject, new HashMap<>(),
					fragmentEntryProcessorContext));
		}

		Element linkElement = new Element("a");

		Elements elements = element.children();

		Element firstChildElement = elements.first();

		boolean processEditableTag = false;

		if (StringUtil.equalsIgnoreCase(element.tagName(), "lfr-editable")) {
			processEditableTag = true;
		}
		else {
			linkElement = element;
		}

		boolean replaceLink = false;

		if ((firstChildElement != null) && processEditableTag &&
			StringUtil.equalsIgnoreCase(firstChildElement.tagName(), "a")) {

			linkElement = firstChildElement;
			replaceLink = true;
		}

		if (configJSONObject.has("target")) {
			linkElement.attr("target", configJSONObject.getString("target"));
		}

		String mappedField = configJSONObject.getString("mappedField");

		if (Validator.isNotNull(href)) {
			linkElement.attr("href", href);
			linkElement.html(
				replaceLink ? firstChildElement.html() : element.html());

			if (processEditableTag) {
				element.html(linkElement.outerHtml());
			}
		}
		else if (assetDisplayPage && Validator.isNotNull(mappedField)) {
			linkElement.attr("href", "${" + mappedField + "}");
			linkElement.html(
				replaceLink ? firstChildElement.html() : element.html());

			if (processEditableTag) {
				element.html(
					_fragmentEntryProcessorHelper.processTemplate(
						linkElement.outerHtml(),
						fragmentEntryProcessorContext));
			}
			else {
				element.replaceWith(
					EditableElementParserUtil.getDocumentBody(
						_fragmentEntryProcessorHelper.processTemplate(
							linkElement.outerHtml(),
							fragmentEntryProcessorContext)));
			}
		}
	}

	@Reference
	private FragmentEntryProcessorHelper _fragmentEntryProcessorHelper;

}
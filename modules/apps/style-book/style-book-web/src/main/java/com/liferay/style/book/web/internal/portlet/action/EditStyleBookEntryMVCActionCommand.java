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

package com.liferay.style.book.web.internal.portlet.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.style.book.constants.StyleBookPortletKeys;
import com.liferay.style.book.exception.NoSuchEntryException;
import com.liferay.style.book.model.StyleBookEntry;
import com.liferay.style.book.service.StyleBookEntryLocalService;
import com.liferay.style.book.service.StyleBookEntryService;
import com.liferay.style.book.web.internal.handler.StyleBookEntryExceptionRequestHandler;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + StyleBookPortletKeys.STYLE_BOOK,
		"mvc.command.name=/style_book/save_draft_style_book_entry"
	},
	service = MVCActionCommand.class
)
public class EditStyleBookEntryMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long styleBookEntryId = ParamUtil.getLong(
			actionRequest, "styleBookEntryId");

		String tokensValues = ParamUtil.getString(
			actionRequest, "tokensValues");

		try {
			StyleBookEntry styleBookEntry =
				_styleBookEntryLocalService.fetchStyleBookEntry(
					styleBookEntryId);

			if (styleBookEntry == null) {
				throw new NoSuchEntryException();
			}

			long draftStyleBookEntryId = styleBookEntryId;

			if (styleBookEntry.isHead()) {
				StyleBookEntry draftStyleBookEntry =
					_styleBookEntryLocalService.getDraft(styleBookEntryId);

				draftStyleBookEntryId =
					draftStyleBookEntry.getStyleBookEntryId();
			}

			_styleBookEntryService.updateTokensValues(
				draftStyleBookEntryId, tokensValues);

			JSONPortletResponseUtil.writeJSON(
				actionRequest, actionResponse,
				JSONFactoryUtil.createJSONObject());
		}
		catch (PortalException portalException) {
			hideDefaultErrorMessage(actionRequest);

			_styleBookEntryExceptionRequestHandler.handlePortalException(
				actionRequest, actionResponse, portalException);
		}
	}

	@Reference
	private StyleBookEntryExceptionRequestHandler
		_styleBookEntryExceptionRequestHandler;

	@Reference
	private StyleBookEntryLocalService _styleBookEntryLocalService;

	@Reference
	private StyleBookEntryService _styleBookEntryService;

}
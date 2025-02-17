<%--
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
--%>

<%@ include file="/init.jsp" %>

<liferay-ui:success key="categoryAdded" message='<%= GetterUtil.getString(MultiSessionMessages.get(renderRequest, "categoryAdded")) %>' />
<liferay-ui:success key="categoryUpdated" message='<%= GetterUtil.getString(MultiSessionMessages.get(renderRequest, "categoryUpdated")) %>' />

<clay:container-fluid
	cssClass="container-view"
>
	<clay:row>
		<clay:col
			lg="3"
		>
			<nav class="menubar menubar-transparent menubar-vertical-expand-lg">
				<ul class="nav nav-nested">
					<li class="nav-item">
						<c:choose>
							<c:when test="<%= ListUtil.isNotEmpty(assetCategoriesDisplayContext.getInheritedVocabularies()) || ListUtil.isNotEmpty(assetCategoriesDisplayContext.getVocabularies()) %>">
								<clay:content-row
									cssClass="mb-4"
									verticalAlign="center"
								>
									<clay:content-col
										expand="<%= true %>"
									>
										<strong class="text-uppercase">
											<liferay-ui:message key="vocabularies" />
										</strong>
									</clay:content-col>

									<clay:content-col>
										<ul class="navbar-nav">
											<li>
												<c:if test="<%= assetCategoriesDisplayContext.hasAddVocabularyPermission() %>">

													<%
													PortletURL editVocabularyURL = assetCategoriesDisplayContext.getEditVocabularyURL();
													%>

													<clay:link
														buttonStyle="borderless"
														href="<%= editVocabularyURL.toString() %>"
														icon="plus"
													/>
												</c:if>
											</li>
											<li>
												<liferay-portlet:actionURL copyCurrentRenderParameters="<%= false %>" name="deleteVocabulary" var="deleteVocabulariesURL">
													<portlet:param name="redirect" value="<%= assetCategoriesDisplayContext.getDefaultRedirect() %>" />
												</liferay-portlet:actionURL>

												<portlet:renderURL var="viewVocabulariesURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
													<portlet:param name="mvcPath" value="/view_vocabularies.jsp" />
												</portlet:renderURL>

												<%
												Map<String, Object> additionalProps = HashMapBuilder.<String, Object>put(
													"deleteVocabulariesURL", deleteVocabulariesURL.toString()
												).put(
													"viewVocabulariesURL", viewVocabulariesURL.toString()
												).build();
												%>

												<clay:dropdown-actions
													additionalProps="<%= additionalProps %>"
													dropdownItems="<%= assetCategoriesDisplayContext.getVocabulariesDropdownItems() %>"
													propsTransformer="js/ActionsComponentPropsTransformer"
												/>
											</li>
										</ul>
									</clay:content-col>
								</clay:content-row>

								<c:if test="<%= ListUtil.isNotEmpty(assetCategoriesDisplayContext.getInheritedVocabularies()) %>">
									<ul class="mb-2 nav nav-stacked">
										<span class="text-truncate"><%= LanguageUtil.get(request, "global") %></span>

										<%
										for (AssetVocabulary vocabulary : assetCategoriesDisplayContext.getInheritedVocabularies()) {
										%>

											<li class="nav-item">

												<%
												PortletURL vocabularyURL = renderResponse.createRenderURL();

												vocabularyURL.setParameter("mvcPath", "/view.jsp");
												vocabularyURL.setParameter("vocabularyId", String.valueOf(vocabulary.getVocabularyId()));
												%>

												<a class="nav-link text-truncate <%= (assetCategoriesDisplayContext.getVocabularyId() == vocabulary.getVocabularyId()) ? "active" : StringPool.BLANK %>" href="<%= vocabularyURL.toString() %>">
													<%= HtmlUtil.escape(vocabulary.getTitle(locale)) %>

													<liferay-ui:icon
														icon="lock"
														iconCssClass="text-muted"
														markupView="lexicon"
													/>
												</a>
											</li>

										<%
										}
										%>

									</ul>
								</c:if>

								<c:if test="<%= ListUtil.isNotEmpty(assetCategoriesDisplayContext.getVocabularies()) %>">
									<ul class="mb-2 nav nav-stacked">
										<span class="text-truncate"><%= assetCategoriesDisplayContext.getGroupName() %></span>

										<%
										for (AssetVocabulary vocabulary : assetCategoriesDisplayContext.getVocabularies()) {
										%>

											<li class="nav-item">

												<%
												PortletURL vocabularyURL = renderResponse.createRenderURL();

												vocabularyURL.setParameter("mvcPath", "/view.jsp");
												vocabularyURL.setParameter("vocabularyId", String.valueOf(vocabulary.getVocabularyId()));
												%>

												<a class="nav-link text-truncate <%= (assetCategoriesDisplayContext.getVocabularyId() == vocabulary.getVocabularyId()) ? "active" : StringPool.BLANK %>" href="<%= vocabularyURL.toString() %>">
													<%= HtmlUtil.escape(vocabulary.getTitle(locale)) %>
												</a>
											</li>

										<%
										}
										%>

									</ul>
								</c:if>
							</c:when>
							<c:otherwise>
								<p class="text-uppercase">
									<strong><liferay-ui:message key="vocabularies" /></strong>
								</p>

								<liferay-frontend:empty-result-message
									actionDropdownItems="<%= assetCategoriesDisplayContext.getVocabularyActionDropdownItems() %>"
									animationType="<%= EmptyResultMessageKeys.AnimationType.NONE %>"
									componentId='<%= liferayPortletResponse.getNamespace() + "emptyResultMessageComponent" %>'
									description='<%= LanguageUtil.get(request, "vocabularies-are-needed-to-create-categories") %>'
									elementType='<%= LanguageUtil.get(request, "vocabularies") %>'
								/>
							</c:otherwise>
						</c:choose>
					</li>
				</ul>
			</nav>
		</clay:col>

		<clay:col
			lg="9"
		>

			<%
			AssetVocabulary vocabulary = assetCategoriesDisplayContext.getVocabulary();
			%>

			<c:if test="<%= vocabulary != null %>">
				<clay:sheet>
					<h2 class="sheet-title">
						<clay:content-row
							verticalAlign="center"
						>
							<clay:content-col>
								<%= HtmlUtil.escape(vocabulary.getTitle(locale)) %>
							</clay:content-col>

							<clay:content-col
								cssClass="inline-item-after"
							>
								<liferay-util:include page="/vocabulary_action.jsp" servletContext="<%= application %>" />
							</clay:content-col>
						</clay:content-row>
					</h2>

					<%
					String linkURL = assetCategoriesDisplayContext.getLinkURL();
					%>

					<c:if test="<%= Validator.isNotNull(linkURL) %>">

						<%
						StringBundler sb = new StringBundler(3);

						sb.append("<a href=\"");
						sb.append(linkURL);
						sb.append("\" target=\"_blank\">");
						%>

						<p>
							<liferay-ui:message arguments='<%= new String[] {sb.toString(), "</a>"} %>' key="x-learn-how-x-to-tailor-categories-to-your-needs" />
						</p>
					</c:if>

					<clay:sheet-section>
						<liferay-util:include page="/view_categories.jsp" servletContext="<%= application %>" />
					</clay:sheet-section>
				</clay:sheet>
			</c:if>
		</clay:col>
	</clay:row>
</clay:container-fluid>
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

import ClayButton from '@clayui/button';
import ClayDropDown, {Align} from '@clayui/drop-down';
import React, {useState} from 'react';

import TokenSet from './TokenSet';
import Toolbar from './Toolbar';
import {config} from './config';

export default function Sidebar() {
	return (
		<div className="style-book-editor__sidebar">
			<Toolbar />
			<SidebarContent />
		</div>
	);
}

function SidebarContent() {
	const tokenCategories = config.tokenCategories;
	const [active, setActive] = useState(false);
	const [selectedCategory, setSelectedCategory] = useState(
		tokenCategories[0]
	);

	return (
		<div className="style-book-editor__sidebar-content">
			{selectedCategory && (
				<ClayDropDown
					active={active}
					alignmentPosition={Align.BottomLeft}
					onActiveChange={setActive}
					trigger={
						<ClayButton
							className="form-control form-control-select form-control-sm mb-3 text-left"
							displayType="secondary"
							small
							type="button"
						>
							{selectedCategory.name}
						</ClayButton>
					}
				>
					<ClayDropDown.ItemList>
						{tokenCategories.map((tokenCategories, index) => (
							<ClayDropDown.Item
								key={index}
								onClick={() => {
									setSelectedCategory(tokenCategories);
									setActive(false);
								}}
							>
								{tokenCategories.name}
							</ClayDropDown.Item>
						))}
					</ClayDropDown.ItemList>
				</ClayDropDown>
			)}

			{selectedCategory?.tokenSets.map(({name, tokens}) => (
				<TokenSet key={name} name={name} tokens={tokens} />
			))}
		</div>
	);
}

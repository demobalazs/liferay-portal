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

import {ClayButtonWithIcon} from '@clayui/button';
import classNames from 'classnames';
import React, {useContext, useEffect, useState} from 'react';

const SidebarContext = React.createContext();

const noop = () => {};

const SidebarBody = ({children}) => {
	return <div className="sidebar-body">{children}</div>;
};

const SidebarHeader = ({children, subtitle, title}) => {
	const {onClose} = useContext(SidebarContext);

	return (
		<div className="sidebar-header">
			<div className="autofit-row sidebar-section">
				<div className="autofit-col autofit-col-expand">
					<div className="component-title">
						<span className="text-truncate-inline">{title}</span>
					</div>

					<p className="component-subtitle">{subtitle}</p>

					{children}
				</div>

				<div className="autofit-col">
					<ClayButtonWithIcon
						displayType="unstyled"
						onClick={onClose}
						small={true}
						symbol="times"
					/>
				</div>
			</div>
		</div>
	);
};

const Sidebar = ({children, onClose = noop, open = true}) => {
	const [isOpen, setIsOpen] = useState(false);

	// Wait until the component is rendered to show it so animation happens

	useEffect(() => {
		if (open !== false) {
			setTimeout(() => setIsOpen(true), 100);
		}
		else {
			setIsOpen(false);
		}
	}, [open]);

	return (
		<div
			className={classNames('sidebar-wrapper', {
				open: isOpen,
			})}
		>
			<div className="sidebar sidebar-light">
				<SidebarContext.Provider value={{onClose}}>
					{children}
				</SidebarContext.Provider>
			</div>
		</div>
	);
};

Sidebar.Body = SidebarBody;
Sidebar.Header = SidebarHeader;

export default Sidebar;

import React from 'react';
import Sidebar from './Sidebar';
import './assets/css/main-layout.css';
import Navbar from './Navbar';

const MainLayout = (props) => {
	return (
		<div className="d-flex" id="wrapper">
			<Sidebar />
			<div id="page-content-wrapper">
				<Navbar />
				<div className="container-fluid p-3">
					{props.children}
				</div>
			</div>
		</div>
	);
};

export default MainLayout;
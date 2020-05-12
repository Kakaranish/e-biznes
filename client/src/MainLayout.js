import React from 'react';
import Sidebar from './Sidebar';
import './assets/css/main-layout.css';
import Navbar from './Navbar';

const MainLayout = (props) => {
	return (
		<div class="d-flex" id="wrapper">
			<Sidebar />
			<div id="page-content-wrapper">
				<Navbar />
				<div class="container-fluid">
					{props.children}
				</div>
			</div>
		</div>
	);
};

export default MainLayout;
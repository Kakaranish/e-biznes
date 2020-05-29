import React from 'react';
import Sidebar from '../skeleton/Sidebar';
import Navbar from '../skeleton/Navbar';
import '../assets/css/main-layout.css';

const MainLayout = (props) => {
	return <>
		<div className="d-flex" id="wrapper">
			<Sidebar />
			<div id="page-content-wrapper">
				<Navbar />
				<div className="container-fluid p-3">

					{props.children}

				</div>
			</div>
		</div>
	</>
};

export default MainLayout;
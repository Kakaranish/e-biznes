import React from 'react';
import $ from 'jquery';

const Navbar = (props) => {

	const handleLogout = () => {
		localStorage.removeItem('token');
		window.location = '/';
	}

	const toggleOnClick = event => {
		event.preventDefault();
		$("#wrapper").toggleClass("toggled");
	};

	return (
		<nav className="navbar navbar-expand-lg navbar-light bg-light border-bottom">
			<button className="btn btn-primary" id="menu-toggle" onClick={toggleOnClick}>Toggle Menu</button>
			<button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
				<span className="navbar-toggler-icon"></span>
			</button>

			{
				props.user &&
				<ul className="navbar-nav ml-auto mt-lg-0">
					<li className="nav-item dropdown">
						<a className="nav-link dropdown-toggle" href="#"
							id="navbarDropdown" role="button" data-toggle="dropdown"
							aria-haspopup="true" aria-expanded="false">
							{props.user.email}
						</a>

						<div className="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown"
							onClick={handleLogout} style={{ cursor: "pointer" }}>
							<span className="dropdown-item">
								Log out
							</span>
						</div>
					</li>
				</ul>
			}
		</nav>
	);
};

export default Navbar;
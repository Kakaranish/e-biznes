import React from 'react';
import $ from 'jquery';

const Navbar = () => {
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
		</nav>
	);
};

export default Navbar;
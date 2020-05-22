import React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import $ from 'jquery';
import cartIcon from '../../src/assets/img/cart.svg';
import Logout from './Logout';

const Navbar = (props) => {

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
				props?.email && <>

					<ul className="navbar-nav ml-auto mt-lg-0">
						<Link to={{ pathname: '/cart' }}>
							<img src={cartIcon} style={{ width: "25px", opacity: "0.4", paddingTop: "7px" }} />
						</Link>

						<li className="nav-item dropdown">
							<a className="nav-link dropdown-toggle" href="#"
								id="navbarDropdown" role="button" data-toggle="dropdown"
								aria-haspopup="true" aria-expanded="false">
								{props?.email}
							</a>

							<div className="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
								<Link to={{ pathname: '/wishlist' }} className="dropdown-item" style={{ cursor: "pointer" }}>
									Wishlist
								</Link>

								<span className="dropdown-item" style={{ cursor: "pointer" }}>
									<Logout />
								</span>
							</div>
						</li>
					</ul>
				</>
			}
		</nav>
	);
};

const mapStateToProps = state => ({
	email: state.auth?.email
})

export default connect(mapStateToProps)(Navbar);
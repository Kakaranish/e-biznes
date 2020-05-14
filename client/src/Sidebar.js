import React from 'react';
import { Link } from 'react-router-dom';

const Sidebar = () => {
    return (
        <>
            <div className="bg-light border-right" id="sidebar-wrapper">
                <div className="sidebar-heading">Start Bootstrap </div>
                <div className="list-group list-group-flush">
                    <Link to={'/'} className="list-group-item list-group-item-action bg-light">
                        Main Page
                    </Link>
                </div>

                <div className="list-group list-group-flush">
                    <Link to={'/categories'} className="list-group-item list-group-item-action bg-light">
                        Categories
                    </Link>
                </div>

                <div className="list-group list-group-flush">
                    <Link to={'/products'} className="list-group-item list-group-item-action bg-light">
                        Products
                    </Link>
                </div>

                <div className="list-group list-group-flush">
                    <Link to={'/users'} className="list-group-item list-group-item-action bg-light">
                        Users
                    </Link>
                </div>
            </div>
        </>
    );
};

export default Sidebar;
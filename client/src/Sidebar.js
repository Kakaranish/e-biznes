import React from 'react';
import { Link } from 'react-router-dom';

const Sidebar = (props) => {
    
    const onClickLogout = () => {
        localStorage.removeItem('token');
    }
    
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
                    <Link to={'/manage/categories'} className="list-group-item list-group-item-action bg-light">
                        Categories
                    </Link>
                </div>

                <div className="list-group list-group-flush">
                    <Link to={'/manage/products'} className="list-group-item list-group-item-action bg-light">
                        Products
                    </Link>
                </div>

                <div className="list-group list-group-flush">
                    <Link to={'/users'} className="list-group-item list-group-item-action bg-light">
                        Users
                    </Link>
                </div>


                <div className='items-separator'></div>

                {
                    !props.user
                        ? <>
                            <div className="list-group list-group-flush">
                                <Link to={'/auth/login'} className="list-group-item list-group-item-action bg-light">
                                    Login
                                </Link>
                            </div>

                            <div className="list-group list-group-flush">
                                <Link to={'/auth/register'} className="list-group-item list-group-item-action bg-light">
                                    Register
                                </Link>
                            </div>
                        </>
                        : <div className="list-group list-group-flush">
                            <Link to={'/'} className="list-group-item list-group-item-action bg-light" onClick={onClickLogout}>
                                Logout
                            </Link>
                        </div>
                }
            </div>
        </>
    );
};

export default Sidebar;
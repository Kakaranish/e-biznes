import React from 'react';
import { Link } from 'react-router-dom';
import AwareComponentBuilder from '../common/AwareComponentBuilder';

const Sidebar = (props) => {

    return (
        <>
            <div className="bg-light border-right" id="sidebar-wrapper">
                
                <div className="sidebar-heading">My e-biznes </div>

                <div className="list-group list-group-flush">
                    <Link to={'/'} className="list-group-item list-group-item-action bg-light">
                        Main Page
                    </Link>
                </div>

                <div className="list-group list-group-flush">
                    <Link to={'/products'} className="list-group-item list-group-item-action bg-light">
                        Products
                    </Link>
                </div>

                <div className="list-group list-group-flush">
                    <Link to={'/categories'} className="list-group-item list-group-item-action bg-light">
                        Categories
                    </Link>
                </div>

                {
                    props.auth?.role === 'ADMIN' && <>
                        <div className='items-separator'></div>
                        <div className="list-group list-group-flush">
                            <Link to={'/manage/categories'} className="list-group-item list-group-item-action bg-light">
                                Manage Categories
                            </Link>

                        </div>

                        <div className="list-group list-group-flush">
                            <Link to={'/manage/products'} className="list-group-item list-group-item-action bg-light">
                                Manage Products
                            </Link>
                        </div>

                        <div className="list-group list-group-flush">
                            <Link to={'/manage/users'} className="list-group-item list-group-item-action bg-light">
                                Manage Users
                            </Link>
                        </div>

                        <div className="list-group list-group-flush">
                            <Link to={'/manage/orders'} className="list-group-item list-group-item-action bg-light">
                                Manage Orders
                            </Link>
                        </div>

                        <div className="list-group list-group-flush">
                            <Link to={'/manage/notifications/create'} className="list-group-item list-group-item-action bg-light">
                                Create Notification
                            </Link>
                        </div>
                    </>
                }

                {
                    !props.auth && <>
                        <div className='items-separator'></div>

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
                }
            </div>
        </>
    );
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(Sidebar);
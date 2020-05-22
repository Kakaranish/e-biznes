import React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';

const Sidebar = (props) => {

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
                            <Link to={'/users'} className="list-group-item list-group-item-action bg-light">
                                Manage Users
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

const mapStateToProps = state => ({
    auth: state?.auth
});

export default connect(mapStateToProps)(Sidebar);
import React from 'react';
import MainLayout from './MainLayout';
import { Route } from 'react-router-dom';

const MainLayoutRoute = ({ component: Component, ...rest }) => {
    return (
        <Route {...rest} render={matchProps => (
            <MainLayout>
                <Component {...matchProps} />
            </MainLayout>
        )} />
    );
};

export default MainLayoutRoute;
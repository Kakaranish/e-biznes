import React from 'react';
import { Route, Switch } from 'react-router-dom';
import LoginPage from './LoginPage';

const AuthRoutes = () => {
    return (
        <Switch>
            <Route path='/auth/login' component={LoginPage} />
        </Switch>
    );
};

export default AuthRoutes;
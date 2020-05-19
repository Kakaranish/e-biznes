import React from 'react';
import { Switch, Route } from 'react-router-dom';
import LoginPage from './LoginPage';
import RegisterPage from './RegisterPage';
import NotAuthorizedOnlyRoute from '../../routes/NotAuthorizedOnlyRoute';
import AuthSuccessfulPage from './AuthSuccessfulPage';
import MainLayoutRoute from '../../routes/MainLayoutRoute';
import AuthFailurePage from './AuthFailurePage';

const AuthRoutes = () => {
	return <Switch>
		<Route path='/auth/successful' component={AuthSuccessfulPage} />
		<MainLayoutRoute path='/auth/failure' component={AuthFailurePage} />
		<NotAuthorizedOnlyRoute path='/auth/login' component={LoginPage} />
		<NotAuthorizedOnlyRoute path='/auth/register' component={RegisterPage} />
	</Switch>
};

export default AuthRoutes;
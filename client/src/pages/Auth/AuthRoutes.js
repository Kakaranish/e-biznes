import React from 'react';
import { Switch } from 'react-router-dom';
import LoginPage from './LoginPage';
import RegisterPage from './RegisterPage';
import NotAuthorizedOnlyRoute from '../../routes/NotAuthorizedOnlyRoute';

const AuthRoutes = () => {
	return <Switch>
		<NotAuthorizedOnlyRoute path='/auth/login' component={LoginPage} />
		<NotAuthorizedOnlyRoute path='/auth/register' component={RegisterPage} />
	</Switch>
};

export default AuthRoutes;
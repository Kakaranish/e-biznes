import React from 'react';
import { Switch } from "react-router-dom";
import UsersPage from './UsersPage';
import UserPage from './UserPage';
import MainLayoutRoute from '../../routes/MainLayoutRoute';

const UserRoutes = () => {
	return <Switch>
		<MainLayoutRoute path='/users' component={UsersPage} exact />
		<MainLayoutRoute path='/users/:id' component={UserPage} />
	</Switch>
};

export default UserRoutes;
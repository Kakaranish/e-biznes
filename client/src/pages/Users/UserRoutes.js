import React from 'react';
import { Switch, Route } from "react-router-dom";
import UsersPage from './UsersPage';
import UserPage from './UserPage';

const UserRoutes = () => {
	return <Switch>
		<Route path='/users' component={UsersPage} exact />
		<Route path='/users/:id' component={UserPage} />
	</Switch>
};

export default UserRoutes;
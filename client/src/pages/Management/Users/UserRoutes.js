import React from 'react';
import { Switch, Route } from "react-router-dom";
import UsersPage from './UsersPage';
import UserPage from './UserPage';
import EditUserPage from './EditUserPage';

const UserRoutes = () => {
	return <Switch>
		<Route exact path='/manage/users' component={UsersPage} />
		<Route path='/manage/users/:id/edit' component={EditUserPage} />
		<Route path='/manage/users/:id' component={UserPage} />
	</Switch>
};

export default UserRoutes;
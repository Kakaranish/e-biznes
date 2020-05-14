import React from 'react';
import { Switch, Route } from "react-router-dom";
import UsersPage from './UsersPage';

const UserRoutes = () => {
	return <>
		<Switch>
			<Route path='/users' component={UsersPage} exact />
		</Switch>
	</>
};

export default UserRoutes;
import React, { useState, useEffect } from 'react';
import { Route, useHistory } from 'react-router-dom';
import * as AuthUtils from '../pages/Auth/Utils';

const NotAuthorizedOnlyRoute = ({ component: Component, ...rest }) => {

    const history = useHistory();
    const [state, setState] = useState({ loading: true });

    useEffect(() => {
        const auth = async () => {
            if (!rest.auth || rest.auth.tokenExpiry < Date.now()) {
                rest.logOut();
                setState({ loading: false });
                return;
            }
            alert('This page requires not to be logged in. Redirecting to main page...');
            history.push('/');
        };
        auth();
    }, []);

    if (state.loading) return <></>
    else return <Route {...rest} render={matchProps => (
        <Component {...matchProps} />
    )} />
};

export default AuthUtils.createAuthAwareComponent(NotAuthorizedOnlyRoute);
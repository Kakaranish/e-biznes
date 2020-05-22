import React, { useState, useEffect } from 'react';
import { Route, useHistory } from 'react-router-dom';
import { verifyAuthState } from '../pages/Utils';
import MainLayout from '../routes/MainLayout'

const NotAuthorizedOnlyRoute = ({ component: Component, ...rest }) => {

    const history = useHistory();
    const [state, setState] = useState({ loading: true, user: null });

    useEffect(() => {
        const auth = async () => {
            const user = await verifyAuthState();
            setState({ loading: false, user: user });
        };
        auth();
    }, []);

    if (state.loading) return <></>
    else return <Route {...rest} render={matchProps => (
        <MainLayout user={state.user}>
            <Component {...matchProps} />
        </MainLayout>
    )} />
};

export default NotAuthorizedOnlyRoute;
import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { isValidUUID, doRequest } from '../../../common/Utils';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';

const UserPage = (props) => {

    const history = useHistory();
    const userId = props.match.params.id;

    const onEdit = () => history.push(`/manage/users/${userId}/edit`);

    const [state, setState] = useState({ loading: true, user: null });
    useEffect(() => {
        const fetchUser = async () => {

            try {
                const action = async () => axios.get(`/api/users/${userId}`, {
                    headers: { 'X-Auth-Token': props.auth.token },
                    validateStatus: false
                });
                const result = await doRequest(action);
                setState({ loading: false, user: result });
            } catch (error) {
                if (error === 404) {
                    setState({ loading: false, user: null });
                    return;
                }
                alert(`${error} error occured`);
            }
        };

        if (isValidUUID(userId)) fetchUser();
    }, []);

    if (!isValidUUID(userId)) return <h3>User Id '{userId}' is invalid UUID</h3>
    else if (state.loading) return <></>
    else if (!state.user) return <h3>User with id '{userId}' does not exist</h3>
    else return <>
        <h3>User {state.user.id}</h3>

        <p>
            <b>Email:</b> {state.user.email}
        </p>

        <p>
            <b>Role:</b> {state.user.role}
        </p>

        <p>
            <b>First name:</b> {state.user.firstName}
        </p>

        <p>
            <b>Last name:</b> {state.user.lastName}
        </p>

        <button type="button" className="btn btn-primary w-25 mr-2" onClick={onEdit}>
            Edit
        </button>
    </>
}

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(UserPage);
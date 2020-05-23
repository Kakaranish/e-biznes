import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { isValidUUID } from '../../common';

const UserPage = (props) => {
    
    const history = useHistory();
    const userId = props.match.params.id;

    const onEdit = () => {

    };

    const onDelete = () => {

    };
    
    const [state, setState] = useState({ loading: true, user: null });
    useEffect(() => {
        const fetchUser = async () => {
            const result = await axios.get(`/api/users/${userId}`,
                { validateStatus: false });

            if (result.status === 404) {
                setState({ loading: false, user: null });
                return;
            }
            if (result.status !== 200) {
                alert('Some error occured');
                return;
            }
            setState({ loading: false, user: result.data });
        };

        if (isValidUUID(userId)) fetchUser();
    }, []);

    if (!isValidUUID(userId)) return <h3>User Id '{userId}' is invalid UUID</h3>
    else if (state.loading) return <></>
    else {
        if (!state.user) return <h3>User with id '{userId}' does not exist</h3>
        else return <>
            <h3>User {state.user.id}</h3>

            <p>
                <b>Email:</b> {state.user.email}
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
};

export default UserPage;
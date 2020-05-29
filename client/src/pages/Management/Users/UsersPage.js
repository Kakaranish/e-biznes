import React, { useState, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import axios from 'axios';
import { doRequest } from '../../../common/Utils';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';

const UsersPage = (props) => {
    const history = useHistory();
    const onCreate = () => history.push('/manage/users/create');

    const [state, setState] = useState({ loading: true, users: null });
    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const action = async () => axios.get('/api/users', {
                    headers: { 'X-Auth-Token': props.auth.token },
                    validateStatus: false
                });
                const result = await doRequest(action);
                setState({ loading: false, users: result });
            } catch (error) {
                alert(`${error} error occured`);
                setState({ loading: false });
            }
        };
        fetchUsers();
    }, []);

    if (state.loading) return <></>
    else {
        if (!state.users || !state.users.length) return <h3>There are no users</h3>
        else return <>
            <h3>Users</h3>
            <ul>
                {
                    state.users.map((user, i) =>
                        <Link to={`/manage/users/${user.id}`} key={`link-${i}`}>
                            <li key={`user-${i}`}>
                                {user.email}
                            </li>
                        </Link>
                    )
                }
            </ul>

            <button className="btn btn-success w-25" onClick={onCreate}>
                Create
			</button>
        </>
    }
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(UsersPage);
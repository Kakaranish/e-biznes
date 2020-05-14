import React, { useState, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import axios from 'axios';

const UsersPage = () => {
    const history = useHistory();
    const onCreate = () => {
        history.push('/users/create');
    }

    const [state, setState] = useState({ loading: true, users: null });
    useEffect(() => {
        const fetchUsers = async () => {
            const result = await axios.get('/api/users', { validateStatus: false });
            if (result.status !== 200) {
                alert('Some error occured');
                setState({ loading: false });
                return;
            }
            setState({ loading: false, users: result.data });
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
                        <Link to={`/users/${user.id}`} key={`link-${i}`}>
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

export default UsersPage;
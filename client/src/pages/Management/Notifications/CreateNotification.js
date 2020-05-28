import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { doRequest, getFormDataJsonFromEvent } from '../../../Utils';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';

const CreateNotification = (props) => {

    const history = useHistory();

    const [state, setState] = useState({ loading: true, users: null });
    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const action = async () => axios.get('/api/users', {
                    validateStatus: false,
                    headers: { 'X-Auth-Token': props.auth.token },
                });
                const result = await doRequest(action);
                setState({ loading: false, users: result });
                console.log(result);
            } catch (error) {
                alert(`${error} error occured`);
                setState({ loading: false });
            }
        };
        fetchUsers();
    }, []);

    const onSubmit = async event => {
        event.preventDefault();
        let formData = getFormDataJsonFromEvent(event);

        try {
            const action = async () => axios.post('/api/notifications', formData, {
                validateStatus: false,
                headers: { 'X-Auth-Token': props.auth.token },
            });
            await doRequest(action);
            history.go();
        } catch (error) {
            alert(`${error} error occured`);
        }
    }

    if (state.loading) return <></>
    else if (state.users === null || state.users.length === 0)
        return <h3>Cannot create notification - no users found</h3>
    else return <>
        <h3>Create notification</h3>

        <form onSubmit={onSubmit}>
            <div className="form-group">
                <label>Content</label>
                <input name="content" type="text" className="form-control" placeholder="Notification content" required />
            </div>

            <div className="form-group">
                <label>Choose user</label>
                <select name="userId" className="custom-select" size="6" required>
                    {
                        state.users.map((u, i) =>
                            <option key={`opt-${u.id}`} value={u.id}>
                                {u.firstName} {u.lastName} ({u.email})
                            </option>
                        )
                    }
                </select>
            </div>

            <button className="btn btn-success w-25" type="submit">
                Create
            </button>
        </form>
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(CreateNotification);
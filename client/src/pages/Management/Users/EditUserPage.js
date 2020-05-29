import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { isValidUUID, doRequest, getFormDataJsonFromEvent } from '../../../common/Utils';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';

const EditUserPage = (props) => {

    const history = useHistory();
    const userId = props.match.params.id;

    const [validationErrors, setValidationErrors] = useState(null);
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

    const onSubmit = async event => {
        event.preventDefault();
        let formData = getFormDataJsonFromEvent(event);

        const result = await axios.put('/api/admin/users', formData, {
            headers: { 'X-Auth-Token': props.auth.token },
            validateStatus: false
        });
        if (result.status === 400) {
            setValidationErrors(result.data.obj.map(r => r.msg));
            return;
        }
        else if (result.status !== 200) {
            alert("Some error occured");
            console.log(result);
            return;
        }

        history.push('/manage/users');
    }

    if (!isValidUUID(userId)) return <h3>User Id '{userId}' is invalid UUID</h3>
    else if (state.loading) return <></>
    else if (!state.user) return <h3>User with id '{userId}' does not exist</h3>
    else return <>
        <form onSubmit={onSubmit}>
            <div className="form-group">
                <label>Id</label>
                <input name="id" type="text" className="form-control" value={state.user.id} readOnly />
            </div>

            <div className="form-group">
                <label>Email</label>
                <input name="email" type="text" className="form-control" placeholder="Email..."
                    defaultValue={state.user.email} required />
            </div>

            <div className="form-group">
                <div className="dropdown">
                    <label>Email</label><br></br>
                    <select name="role" className="custom-select" required>
                        {
                            ["USER", "ADMIN"].map((role) =>
                                <option selected={state.user.role == role} key={`opt-${role}`} value={role}>
                                    {role}
                                </option>
                            )
                        }
                    </select>
                </div>
            </div>

            <div className="form-group">
                <label>First name</label>
                <input name="firstName" type="text" className="form-control" placeholder="First name..."
                    defaultValue={state.user.firstName} required />
            </div>

            <div className="form-group">
                <label>Last name</label>
                <input name="lastName" type="text" className="form-control" placeholder="Last name..."
                    defaultValue={state.user.lastName} required />
            </div>

            <button type="submit" className="btn btn-success w-25">
                Update User
            </button>

            {
                validationErrors &&
                <div className="col-12 mt-2">
                    <p className="text-danger font-weight-bold" style={{ marginBottom: '0px' }}>
                        Validation errors
                    </p>
                    <ul style={{ paddingTop: "0" }, { marginTop: "0px" }}>
                        {
                            validationErrors.map((error, i) => {
                                return <li key={`val-err-${i}`} className="text-danger">{error}</li>
                            })
                        }
                    </ul>
                </div>
            }
        </form>
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(EditUserPage);
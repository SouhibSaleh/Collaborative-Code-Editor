export interface UpdateCharacter {
    userId: number;
    index: number;
    value: string;

}
export interface Snapshot {
    projectId: number;
    code: string;
    comments: string;
}
export interface Message {
    id: number;
    username: string;
    message: string;
    date: string;
}
export interface Project {
    id: number;
    userId: number;
    snapshotId: number;
    name: string;
    language: string;
    creationDate: string;
}
export interface GetSnapshot {
    id: number;
    code: string;
    projectId: number;
    userId: number;
    creationDate: string;
    comments: string
}
export interface updateSnapshotRequest {
    snapshotId: number;
    code: string;
}
export interface ProjectRequest {
    name: string;
    language: string;
}
export interface Languages {
    id: number;
    language: string;
}
export interface RunCodeRequest {
    snapshotId: number;
    code: string;
    language: string;
    args: string;

}
export interface lockLineRequest {
    userId: number;
    lineNumber: number;
}
export interface enterProjectRequest {
    username: string,
    enter: boolean
}